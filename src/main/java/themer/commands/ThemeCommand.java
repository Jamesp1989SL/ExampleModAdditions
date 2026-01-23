package themer.commands;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Random;

import themer.themes.BiomeTheme;
import themer.themes.BiomeThemeRegistry;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.StringParameterHandler;
import necesse.engine.network.packet.PacketRegionData;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BiomeRegistry;
import necesse.engine.registries.ObjectLayerRegistry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.util.TicketSystemList;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobSpawnTable;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.regionSystem.Region;

public class ThemeCommand extends ModularChatCommand {

    public ThemeCommand() {
        super(
                "theme",
                "Applies biome overlay + wipes objects + re-themes area around you",
                PermissionLevel.ADMIN,
                true,
                new CmdParameter("biome", new StringParameterHandler(null, BiomeRegistry.getBiomeStringIDs())),
                new CmdParameter("radius", new IntParameterHandler(100), true)
        );
    }

    /**
     * Detects the "includes contains null" issue that will crash MobSpawnTable.addCanSpawns(...)
     * when the biome is queried during ticking/spawning.
     */
    private static boolean hasNullIncludes(MobSpawnTable table) {
        if (table == null) return false;

        try {
            Field f = MobSpawnTable.class.getDeclaredField("includes");
            f.setAccessible(true);

            @SuppressWarnings("unchecked")
            LinkedList<MobSpawnTable> includes = (LinkedList<MobSpawnTable>) f.get(table);

            if (includes == null) return false;

            for (MobSpawnTable inc : includes) {
                if (inc == null) return true;
                if (hasNullIncludes(inc)) return true; // recursive
            }
            return false;
        } catch (Exception e) {
            // If reflection fails for some reason, don't block the command.
            return false;
        }
    }

    @Override
    public void runModular(necesse.engine.network.client.Client client, Server server, ServerClient serverClient,
                           Object[] args, String[] errors, CommandLog logs) {

        if (serverClient == null || serverClient.playerMob == null) {
            logs.add("This command must be run by a player.");
            return;
        }

        String biomeStringID = (String) args[0];
        int radius = (Integer) args[1];

        int biomeID;
        try {
            biomeID = BiomeRegistry.getBiomeIDRaw(biomeStringID);
        } catch (Exception e) {
            logs.add("Unknown biome: " + biomeStringID);
            return;
        }

        Level level = serverClient.getLevel();

        // --- Guard: refuse to apply biomes whose MobSpawnTable includes contain nulls (will crash ticking).
        try {
            Biome biome = BiomeRegistry.getBiome(biomeID);
            if (biome != null) {
                MobSpawnTable mobTable = biome.getMobSpawnTable(level);
                MobSpawnTable critterTable = biome.getCritterSpawnTable(level);

                if (hasNullIncludes(mobTable) || hasNullIncludes(critterTable)) {
                    logs.add("Refusing to apply biome '" + biome.getStringID()
                            + "': its MobSpawnTable includes a null entry (include(null)), which will crash ticking/spawning.");
                    return;
                }
            }
        } catch (Exception e) {
            // If anything goes wrong in the guard, don't stop the command.
        }

        BiomeTheme theme = BiomeThemeRegistry.get(biomeStringID);
        if (theme == null) {
            logs.add("No theme registered for '" + biomeStringID + "'. (It will only change biome overlay.)");
        } else {
            if (!theme.isUsable()) {
                logs.add(theme.getDisabledMessage());
                return;
            }
        }

        int cx = serverClient.playerMob.getTileX();
        int cy = serverClient.playerMob.getTileY();

        int startX = level.limitTileXToBounds(cx - radius);
        int endX   = level.limitTileXToBounds(cx + radius);
        int startY = level.limitTileYToBounds(cy - radius);
        int endY   = level.limitTileYToBounds(cy + radius);

        Ellipse2D circle = new Ellipse2D.Float(startX, startY, (endX - startX), (endY - startY));

        int startRegionX = level.regionManager.getRegionCoordByTile(startX);
        int startRegionY = level.regionManager.getRegionCoordByTile(startY);
        int endRegionX   = level.regionManager.getRegionCoordByTile(endX);
        int endRegionY   = level.regionManager.getRegionCoordByTile(endY);

        final int regionSize = 16;
        Random random = new GameRandom(level.getIdentifier().hashCode() ^ (cx * 73856093L) ^ (cy * 19349663L));

        GameObject air = ObjectRegistry.getObject("air");
        TicketSystemList<GameObject> tickets = (theme != null) ? theme.createObjectTickets(level) : null;

        logs.add("Applying theme biome=" + biomeStringID + " radius=" + radius);

        for (int regionY = startRegionY; regionY <= endRegionY; regionY++) {
            for (int regionX = startRegionX; regionX <= endRegionX; regionX++) {

                Rectangle regionRect = new Rectangle(regionX * regionSize, regionY * regionSize, regionSize, regionSize);
                if (!circle.intersects(regionRect) && !circle.contains(regionRect)) continue;

                boolean wasLoaded = level.regionManager.isRegionLoaded(regionX, regionY);
                level.regionManager.ensureRegionIsLoadedButDontGenerate(regionX, regionY);

                Region region = level.regionManager.getRegion(regionX, regionY, true);
                if (region == null) continue;

                boolean fillAll = circle.contains(regionRect);

                // PASS A: biome overlay + paint tile + WIPE all objects (all layers)
                for (int y = regionY * regionSize; y < (regionY + 1) * regionSize; y++) {
                    for (int x = regionX * regionSize; x < (regionX + 1) * regionSize; x++) {

                        if (!level.isTileWithinBounds(x, y)) continue;
                        if (!fillAll && !circle.contains(x, y)) continue;

                        int rx = x - region.tileXOffset;
                        int ry = y - region.tileYOffset;

                        region.biomeLayer.setBiomeByRegion(rx, ry, biomeID, true);

                        if (theme != null) {
                            theme.paintTile(level, x, y, random);
                        }

                        for (int layerID : ObjectLayerRegistry.getLayerIDs()) {
                            if (level.getObjectID(layerID, x, y) != 0) {
                                air.placeObject(level, layerID, x, y, 0, false);
                            }
                        }
                    }
                }

                // PASS B: place themed objects (multi-tile safe)
                if (theme != null && tickets != null) {
                    for (int x = regionX * regionSize; x < (regionX + 1) * regionSize; x++) {
                        for (int y = regionY * regionSize; y < (regionY + 1) * regionSize; y++) {

                            if (!level.isTileWithinBounds(x, y)) continue;
                            if (!fillAll && !circle.contains(x, y)) continue;

                            GameObject obj = tickets.getRandomObject(random);
                            if (obj.getID() == air.getID()) continue;

                            int rot = random.nextInt(4);

                            if (obj.canPlace(level, x, y, rot, false) != null) continue;

                            MultiTile multi = obj.getMultiTile(rot);

                            // footprint must be empty
                            if (multi.streamOtherIDs(x, y).anyMatch(c -> (level.getObjectID(c.tileX, c.tileY) != 0))) continue;

                            // adjacency rule (theme-controlled)
                            if (theme.requireAdjacentClear(obj)) {
                                if (multi.getAdjacentTiles(x, y, true).stream().anyMatch(p -> (level.getObjectID(p.x, p.y) != 0))) continue;
                            }

                            obj.placeObject(level, x, y, rot, false);
                        }
                    }

                    // PASS C: decorate
                    for (int y = regionY * regionSize; y < (regionY + 1) * regionSize; y++) {
                        for (int x = regionX * regionSize; x < (regionX + 1) * regionSize; x++) {
                            if (!level.isTileWithinBounds(x, y)) continue;
                            if (!fillAll && !circle.contains(x, y)) continue;
                            theme.decorate(level, x, y, random);
                        }
                    }
                }

                // Sync region once
                int finalRX = regionX;
                int finalRY = regionY;

                ServerClient[] clientsWithRegion = server.streamClients()
                        .filter(c -> c.hasRegionLoaded(level, finalRX, finalRY))
                        .toArray(ServerClient[]::new);

                if (clientsWithRegion.length > 0) {
                    PacketRegionData packet = new PacketRegionData(region);
                    for (ServerClient c : clientsWithRegion) {
                        c.sendPacket(packet);
                    }
                }

                if (!wasLoaded) {
                    level.regionManager.unloadRegion(regionX, regionY);
                }
            }
        }

        logs.add("Done.");
    }
}
