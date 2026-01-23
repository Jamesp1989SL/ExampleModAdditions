package examplemod;

import examplemod.examples.themes.ThemeInit;
import necesse.engine.modLoader.annotations.ModEntry;


@ModEntry
public class ExampleMod {

    public void init() {
    }

    public void initResources() {
    }

    public void postInit() {

        ThemeInit.registerAll();
    }
}
