package examplemod.Loaders;

import examplemod.examples.settlement.settlers.ExampleSettler;
import necesse.engine.registries.SettlerRegistry;


public class ExampleModSettlers {

    public static void load(){
        // Register our settler ExampleSettler used by ExampleSettlerMob //DEBUG
        SettlerRegistry.registerSettler("examplesettler", new ExampleSettler());
    }

}
