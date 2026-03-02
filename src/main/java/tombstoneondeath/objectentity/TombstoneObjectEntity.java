package tombstoneondeath.objectentity;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.level.maps.Level;
import necesse.entity.objectEntity.InventoryObjectEntity;

/**
 * Tombstone storage object entity.
 * - Names the container as "<owner>'s Tombstone"
 * - Prevents self-removal until it has actually contained items at least once
 * - Removes itself once it becomes empty again after being looted
 */
public class TombstoneObjectEntity extends InventoryObjectEntity {

    private long ownerUniqueID = -1L;
    private String ownerName = "";
    public boolean hasEverHadItems = false;

    public TombstoneObjectEntity(Level level, int x, int y, int slots) {
        super(level, x, y, slots);
    }

    /** Called after placement. */
    public void setOwner(long ownerUniqueID, String ownerName) {
        this.ownerUniqueID = ownerUniqueID;
        this.ownerName = ownerName == null ? "" : ownerName;

        // this is what the container UI actually uses (InventoryObjectEntity.name)
        // so each tombstone gets its own correct label.
        if (!this.ownerName.isEmpty()) {
            setInventoryName(this.ownerName + "'s Tombstone");
        } else {
            setInventoryName("Tombstone");
        }

        markDirty();
    }

    public long getOwnerUniqueID() {
        return ownerUniqueID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addLong("ownerUniqueID", ownerUniqueID);
        save.addSafeString("ownerName", ownerName);
        save.addBoolean("hasEverHadItems", hasEverHadItems);
    }

    @Override
    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);

        ownerUniqueID = save.getLong("ownerUniqueID", ownerUniqueID, false);
        ownerName = save.getSafeString("ownerName", ownerName);

        // If old saves don’t have the flag, infer from inventory contents
        hasEverHadItems = save.getBoolean("hasEverHadItems", hasAnyItems(), false);

        // Re-apply the display name after load (super loads "name" too, but this keeps it consistent)
        if (!ownerName.isEmpty()) {
            setInventoryName(ownerName + "'s Tombstone");
        }
    }

    @Override
    public void setupContentPacket(PacketWriter writer) {
        super.setupContentPacket(writer);
        writer.putNextLong(ownerUniqueID);
        writer.putNextString(ownerName);
        writer.putNextBoolean(hasEverHadItems);
    }

    @Override
    public void applyContentPacket(PacketReader reader) {
        super.applyContentPacket(reader);
        ownerUniqueID = reader.getNextLong();
        ownerName = reader.getNextString();
        hasEverHadItems = reader.getNextBoolean();
    }

    @Override
    protected void onInventorySlotUpdated(int slot) {
        super.onInventorySlotUpdated(slot);

        if (!isServer()) return;

        // First time the tombstone actually has items, arm the auto-delete.
        if (!hasEverHadItems && hasAnyItems()) {
            hasEverHadItems = true;
            markDirty();
            return;
        }

        // Only remove once it HAS had items before, and is now empty.
        if (hasEverHadItems && !hasAnyItems()) {
            removeSelfFromWorld();
        }
    }

    public boolean hasAnyItems() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!inventory.isSlotClear(i)) return true;
        }
        return false;
    }

    public void removeSelfFromWorld() {
        Level level = getLevel();
        if (level == null || !level.isServer()) return;

        // Remove object tile (layer 0) + sync to clients
        level.sendObjectChangePacket(level.getServer(), tileX, tileY, 0, 0);

        // Safety: remove this object entity too
        remove();
    }


}