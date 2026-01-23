package themer.themes;

import java.util.Random;

import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

public interface BiomeTheme {
    /** Biome string id (e.g. "graveyard", "forestdeepcave"). */
    String getBiomeStringID();

    /** Called for every tile in the radius to paint the ground. */
    void paintTile(Level level, int tileX, int tileY, Random random);

    /** Weighted pool of objects to place after wipe. Include "air" with high weight for sparse placement. */
    TicketSystemList<GameObject> createObjectTickets(Level level);

    /**
     * If true, object placement also requires the adjacent tiles around the multitile footprint to be empty.
     * Some themes (caves) look better with clusters, so they can return false.
     */
    default boolean requireAdjacentClear(GameObject obj) {
        return true;
    }

    /** Optional decorator pass after object placement (grass patches etc.). */
    default void decorate(Level level, int tileX, int tileY, Random random) {
    }

    /** If false, /theme should refuse to run this theme and show getDisabledMessage(). */
    default boolean isUsable() {
        return true;
    }

    /** Message shown when isUsable() is false. */
    default String getDisabledMessage() {
        return "This theme is not usable.";
    }
}
