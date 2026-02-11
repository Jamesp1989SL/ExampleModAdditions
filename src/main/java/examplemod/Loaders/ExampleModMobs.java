package examplemod.Loaders;

import examplemod.examples.mobs.ExampleBossMob;
import examplemod.examples.mobs.ExampleMob;
import examplemod.examples.mobs.ExampleSummonWeaponMob;
import examplemod.examples.mobs.ExampleSettlerMob;
import necesse.engine.registries.MobRegistry;

public class ExampleModMobs {
    public static void load(){
        // Register our mob
        MobRegistry.registerMob("examplemob", ExampleMob.class, true);

        // Register boss mob
        MobRegistry.registerMob("examplebossmob", ExampleBossMob.class,true,true);

        // Register summon mob
        MobRegistry.registerMob("examplesummonmob", ExampleSummonWeaponMob.class,true,false);

        // Register a example mob (ExampleSettlerMob that uses ExampleSettler for settler settings and is capable of our ExampleLevelJob //DEBUG
        MobRegistry.registerMob("examplesettlermob", ExampleSettlerMob.class, false, false,true);
    }
}
