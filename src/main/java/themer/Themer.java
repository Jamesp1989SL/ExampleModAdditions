package themer;

import themer.themes.ThemeInit;
import necesse.engine.modLoader.annotations.ModEntry;


@ModEntry
public class Themer {

    public void init() {
    }

    public void initResources() {
    }

    public void postInit() {

        ThemeInit.registerAll();
    }
}
