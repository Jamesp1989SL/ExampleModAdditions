package tombstoneondeath.patches;

import java.util.Iterator;
import java.util.LinkedList;

import necesse.engine.GameDeathPenalty;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketAddDeathLocation;
import necesse.engine.network.packet.PacketPlayerDie;
import necesse.engine.network.server.ItemDropperHandler;
import necesse.engine.network.server.ServerClient;
import necesse.engine.playerStats.PlayerStats;
import necesse.engine.util.LevelDeathLocation;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.util.WorldDeathLocation;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryItem;
import necesse.inventory.InventorySlot;
import necesse.inventory.PlayerInventory;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.PlayerTempInventory;
import necesse.inventory.item.toolItem.ToolDamageItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.maps.Level;

import net.bytebuddy.asm.Advice;
import tombstoneondeath.TombstoneUtils;
import tombstoneondeath.objectentity.TombstoneObjectEntity;

/**
 * Patch: ServerClient.die(int)
 *
 * Vanilla drops items as pickup entities based on the world deathPenalty.
 * This patch replaces that dropping with a placed tombstone object that stores the items.
 *
 * IMPORTANT ByteBuddy rule:
 * - Do NOT use lambdas/streams/method refs inside Advice logic (causes IllegalAccessError).
 * - Any helper called from Advice must be PUBLIC STATIC (because the caller is effectively ServerClient).
 */
@ModMethodPatch(target = ServerClient.class, name = "die", arguments = { int.class })
public class TombstoneDeathPatch {

    /** dropper that stores drop results into tombstone. */
    public static class TombstoneDropper implements ItemDropperHandler {
        private final TombstoneObjectEntity tombstone;

        public TombstoneDropper(TombstoneObjectEntity tombstone) {
            this.tombstone = tombstone;
        }

        @Override
        public void dropItem(InventoryItem item, PlayerInventorySlot slot, boolean locked) {
            if (item != null) {
                TombstoneUtils.addToTombstone(tombstone, item.copy());
            }
        }
    }

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static boolean onEnter(
            @Advice.This ServerClient client,
            @Advice.Argument(0) int respawnTime,
            @Advice.FieldValue(value = "isDead", readOnly = false) boolean isDead,
            @Advice.FieldValue(value = "respawnTime", readOnly = false) long respawnTimeField,
            @Advice.FieldValue(value = "deathLocations") LinkedList<WorldDeathLocation> deathLocations,
            @Advice.FieldValue(value = "characterStats") PlayerStats characterStats,
            @Advice.FieldValue(value = "newStats") PlayerStats newStats
    ) {
        if (isDead) {
            return false; // let vanilla handle
        }

        PlayerMob player = client.playerMob;
        Level level = (player == null) ? null : player.getLevel();

        // Only run this replacement on the server with a valid level.
        if (player == null || level == null || level.isClient()) {
            return false;
        }

        // Place tombstone first. If we can't place it, fall back to vanilla drops.
        TombstoneObjectEntity tombstone = TombstoneUtils.placeTombstone(client, player.getX(), player.getY());
        if (tombstone == null) {
            return false;
        }

        // --- From here on we fully replace ServerClient.die(int) ---
        isDead = true;
        newStats.deaths.increment(1);
        client.closeContainer(false);

        GameDeathPenalty penalty = client.getServer().world.settings.deathPenalty;

        if (penalty == GameDeathPenalty.DROP_MATS) {
            // Keep vanilla conversion logic, but store the produced drops in the tombstone.
            ItemDropperHandler dropper = new TombstoneDropper(tombstone);

            Iterator<PlayerInventorySlot> it = player.getInv()
                    .streamPlayerSlots(true, false, true, true)
                    .iterator();

            while (it.hasNext()) {
                PlayerInventorySlot slot = it.next();

                if (!slot.isSlotClear(player.getInv())) {
                    boolean locked = slot.isItemLocked(player.getInv());
                    InventoryItem item = slot.getItem(player.getInv());

                    if (item != null) {
                        // This will call dropper.dropItem(...) for each produced mat item.
                        if (item.item.dropAsMatDeathPenalty(slot, locked, item, dropper)) {
                            slot.setItem(player.getInv(), null);
                        }
                    }
                }
            }

        } else if (penalty == GameDeathPenalty.DROP_MAIN_INVENTORY) {
            moveMainInventoryToTombstone(player, tombstone);

        } else if (penalty == GameDeathPenalty.DROP_FULL_INVENTORY || penalty == GameDeathPenalty.HARDCORE) {
            moveFullInventoryToTombstone(player, tombstone);
        }

        // Vanilla: give a wood axe if the player doesn't have an axe.
        if (!hasAxe(player)) {
            player.getInv().addItem(new InventoryItem("woodaxe", 1), true, "respawnitem", null);
        }

        // Vanilla: respawn timer + death locations + packets
        respawnTimeField = client.getServer().world.worldEntity.getTime() + respawnTime;

        LevelIdentifier levelId = client.getLevelIdentifier();
        deathLocations.addLast(new WorldDeathLocation(characterStats, levelId, player.getX(), player.getY()));
        client.sendPacket((Packet) new PacketAddDeathLocation(new LevelDeathLocation(0, player.getX(), player.getY())));
        client.getServer().network.sendToAllClients((Packet) new PacketPlayerDie(client.slot, respawnTime));

        return true; // skip vanilla die()
    }

    /**
     * Mirrors vanilla "has axe" check, but without any lambdas.
     * NOTE: streamInventorySlots returns InventorySlot (not PlayerInventorySlot).
     */
    public static boolean hasAxe(PlayerMob player) {
        Iterator<InventorySlot> it = player.getInv()
                .streamInventorySlots(true, false, true, true)
                .iterator();

        while (it.hasNext()) {
            InventorySlot slot = it.next();

            if (!slot.isSlotClear()) {
                InventoryItem item = slot.getItem();
                if (item != null && item.item instanceof ToolDamageItem) {
                    ToolType toolType = ((ToolDamageItem) item.item).getToolType();
                    if (toolType == ToolType.ALL || toolType == ToolType.AXE) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** Move main inventory (excluding hotbar 0-9) into the tombstone. */
    public static void moveMainInventoryToTombstone(PlayerMob player, TombstoneObjectEntity tombstone) {
        PlayerInventory main = player.getInv().main;

        for (int i = 10; i < main.getSize(); i++) {
            if (!main.isSlotClear(i)) {
                InventoryItem item = main.getItem(i);
                if (item != null) {
                    TombstoneUtils.addToTombstone(tombstone, item.copy());
                    main.clearSlot(i);
                }
            }
        }
    }

    /** Move full inventory into the tombstone (matching vanilla dropInventory layout). */
    public static void moveFullInventoryToTombstone(PlayerMob player, TombstoneObjectEntity tombstone) {
        moveInventory(player.getInv().drag, tombstone);
        moveInventory(player.getInv().main, tombstone);

        // Equipment (includes inactive sets). This stream yields PlayerInventory.
        Iterator<PlayerInventory> eqIt = player.getInv().equipment.streamAllInventories().iterator();
        while (eqIt.hasNext()) {
            moveInventory(eqIt.next(), tombstone);
        }

        moveInventory(player.getInv().trash, tombstone);

        // Temp inventories is Iterable<PlayerTempInventory> in 1.1.1
        for (PlayerTempInventory inv : player.getInv().getTempInventories()) {
            moveInventory(inv, tombstone);
        }
    }

    /** Generic inventory mover. */
    public static void moveInventory(Inventory inv, TombstoneObjectEntity tombstone) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (!inv.isSlotClear(i)) {
                InventoryItem item = inv.getItem(i);
                if (item != null) {
                    TombstoneUtils.addToTombstone(tombstone, item.copy());
                    inv.setItem(i, null);
                }
            }
        }
    }
}