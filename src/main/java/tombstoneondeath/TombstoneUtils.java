package tombstoneondeath;

import java.awt.Point;

import necesse.engine.GameLog;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ObjectRegistry;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.inventory.InventoryItem;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import tombstoneondeath.objectentity.TombstoneObjectEntity;

public final class TombstoneUtils {
    private TombstoneUtils() {
    }

    // Tries to place the tombstone object at/near the given pixel position.
    // Returns the object entity if it was placed, otherwise null.
    public static TombstoneObjectEntity placeTombstone(ServerClient client, int pixelX, int pixelY) {
        if (client == null || client.playerMob == null) return null;

        Level level = client.playerMob.getLevel();
        if (level == null || level.isClient()) return null;

        int baseTileX = pixelX / 32;
        int baseTileY = pixelY / 32;

        int objID = TombstoneOnDeathMod.TOMBSTONE_OBJECT_ID;
        GameObject obj = ObjectRegistry.getObject(objID);

        Point spot = findPlaceSpot(level, obj, baseTileX, baseTileY);
        if (spot == null) {
            GameLog.warn.println("[TombstoneOnDeath] Could not find valid place spot near " + baseTileX + "," + baseTileY);
            return null;
        }

        // Use proper placement so the object + object entity are created correctly server-side
        obj.placeObject(level, spot.x, spot.y, 0, false);

        // IMPORTANT:
        // In 1.1.1, placeObject() does not reliably network-sync the object change before OE packets.
        // Force an object update packet so clients know the tombstone object exists at this tile.
        level.sendObjectUpdatePacket(spot.x, spot.y);

        // Fetch the object entity created by placement
        ObjectEntity oe = level.entityManager.getObjectEntity(spot.x, spot.y);
        if (oe instanceof TombstoneObjectEntity) {
            TombstoneObjectEntity toe = (TombstoneObjectEntity) oe;
            toe.setOwner(client.getCharacterUniqueID(), client.getName());
            GameLog.out.println("[TombstoneOnDeath] Placed tombstone at " + spot.x + "," + spot.y + " for " + client.getName());
            return toe;
        }

        GameLog.warn.println("[TombstoneOnDeath] Tombstone placed but ObjectEntity was " + (oe == null ? "null" : oe.getClass().getName()));
        return null;
    }

    // Adds an item to the tombstone inventory (stacking when possible).
    // Returns true if the item was fully inserted.
    public static boolean addToTombstone(TombstoneObjectEntity tombstone, InventoryItem item) {
        if (tombstone == null || item == null) return false;
        return tombstone.inventory.addItem(null, null, item, "tombstone", null);
    }

    // Find a nearby tile where the object can actually be placed.
    private static Point findPlaceSpot(Level level, GameObject obj, int tileX, int tileY) {
        if (canPlaceHere(level, obj, tileX, tileY)) return new Point(tileX, tileY);

        for (int r = 1; r <= 6; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    if (Math.abs(dx) != r && Math.abs(dy) != r) continue; // ring edge only
                    int x = tileX + dx;
                    int y = tileY + dy;
                    if (canPlaceHere(level, obj, x, y)) return new Point(x, y);
                }
            }
        }

        return null;
    }

    private static boolean canPlaceHere(Level level, GameObject obj, int tileX, int tileY) {
        if (!level.isTileWithinBounds(tileX, tileY)) return false;

        // Must be empty in layer 0 AND pass object placement rules
        if (level.getObjectID(tileX, tileY) != 0) return false;

        // canPlace returns null when it's valid
        return obj.canPlace(level, 0, tileX, tileY, 0, false, false) == null;
    }
}