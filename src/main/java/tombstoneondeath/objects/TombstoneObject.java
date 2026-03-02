package tombstoneondeath.objects;

import java.awt.Color;
import java.util.HashMap;

import necesse.engine.network.packet.PacketChatMessage;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.container.StorageBoxInventoryObject;
import necesse.level.maps.Level;
import tombstoneondeath.objectentity.TombstoneObjectEntity;

public class TombstoneObject extends StorageBoxInventoryObject {

    private static final HashMap<Long, Long> lastWarnTime = new HashMap<>();
    private static final long WARN_COOLDOWN_MS = 800;

    public TombstoneObject() {
        super("tombstoneondeath_tombstone", 120, ToolType.NONE, new Color(120, 120, 120));
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new TombstoneObjectEntity(level, x, y, this.slots);
    }

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        // Client: allow the click. Server will decide what actually happens.
        if (level.isClient()) return true;
        if (player == null || player.getServerClient() == null) return false;

        TombstoneObjectEntity toe = getTombstoneOE(level, x, y);
        if (toe == null) return false;

        // Make sure CanInteract returns true even if its empty
        if (!toe.hasAnyItems()) {
            return true;
        }

        // Non-empty: only owner can open
        long owner = toe.getOwnerUniqueID();
        long me = player.getServerClient().getCharacterUniqueID();

        boolean allowed = owner != -1L && owner == me;
        if (!allowed) {
            maybeWarnNotYours(player);
        }
        return allowed;
    }

    @Override
    public void interact(Level level, int x, int y, PlayerMob player) {
        // Server-side: handle removal / owner enforcement here.
        if (level.isServer()) {
            if (player == null || player.getServerClient() == null) return;

            TombstoneObjectEntity toe = getTombstoneOE(level, x, y);
            if (toe == null) return;

            // Delete on click if empty
            if (!toe.hasAnyItems()) {
                toe.removeSelfFromWorld();
                return;
            }

            // If not empty only the owner can open
            long owner = toe.getOwnerUniqueID();
            long me = player.getServerClient().getCharacterUniqueID();

            if (owner != -1L && owner != me) {
                maybeWarnNotYours(player);
                return;
            }
        }

        // If owner
        super.interact(level, x, y, player);
    }

    @Override
    public String getInteractTip(Level level, int x, int y, PlayerMob perspective, boolean debug) {
        // Tip is shown client-side. If the client knows it's empty, show "Remove".
        if (level.isClient()) {
            TombstoneObjectEntity toe = getTombstoneOE(level, x, y);
            if (toe != null && !toe.hasAnyItems()) {
                return "Remove";
            }
        }
        return super.getInteractTip(level, x, y, perspective, debug);
    }

    private static void maybeWarnNotYours(PlayerMob player) {
        long me = player.getServerClient().getCharacterUniqueID();
        long now = System.currentTimeMillis();

        Long last = lastWarnTime.get(me);
        if (last == null || (now - last) >= WARN_COOLDOWN_MS) {
            lastWarnTime.put(me, now);
            player.getServerClient().sendPacket(new PacketChatMessage("Not your tombstone"));
        }
    }

    private TombstoneObjectEntity getTombstoneOE(Level level, int x, int y) {
        ObjectEntity oe = level.entityManager.getObjectEntity(x, y);
        return (oe instanceof TombstoneObjectEntity) ? (TombstoneObjectEntity) oe : null;
    }
}