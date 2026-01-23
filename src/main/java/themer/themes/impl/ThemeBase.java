package themer.themes.impl;

import necesse.level.maps.Level;

public abstract class ThemeBase {

    protected static void setTile(Level level, int x, int y, int tileID) {
        if (level.getTileID(x, y) != tileID) {
            level.setTile(x, y, tileID);
        }
    }
}
