package themer.themes;

import themer.commands.ThemeCommand;
import themer.themes.impl.*;
import necesse.engine.commands.CommandsManager;

/**
 * Call ThemeInit.registerAll() from your mod entry init() to enable /theme.
 */
public final class ThemeInit {
    private ThemeInit() {
    }

    public static void registerAll() {
        // command
        CommandsManager.registerServerCommand(new ThemeCommand());

        // themes (one per BiomeRegistry biome)
        BiomeThemeRegistry.register(new UnknownTheme());
        BiomeThemeRegistry.register(new ForestTheme());
        BiomeThemeRegistry.register(new PlainsTheme());
        BiomeThemeRegistry.register(new DesertTheme());
        BiomeThemeRegistry.register(new SwampTheme());
        BiomeThemeRegistry.register(new SnowTheme());
        BiomeThemeRegistry.register(new DungeonTheme());
        BiomeThemeRegistry.register(new PirateVillageTheme());
        BiomeThemeRegistry.register(new TempleTheme());
        BiomeThemeRegistry.register(new TrialRoomTheme());

        BiomeThemeRegistry.register(new ForestDeepCaveTheme());
        BiomeThemeRegistry.register(new SnowDeepCaveTheme());
        BiomeThemeRegistry.register(new SwampDeepCaveTheme());
        BiomeThemeRegistry.register(new DesertDeepCaveTheme());

        BiomeThemeRegistry.register(new SlimeCaveTheme());
        BiomeThemeRegistry.register(new GraveyardTheme());
        BiomeThemeRegistry.register(new SpiderCastleTheme());
        BiomeThemeRegistry.register(new SunArenaTheme());
        BiomeThemeRegistry.register(new MoonArenaTheme());
        BiomeThemeRegistry.register(new CrystalHollowTheme());
        BiomeThemeRegistry.register(new SettlementRuinsTheme());
        BiomeThemeRegistry.register(new AscendedVoidTheme());
    }
}
