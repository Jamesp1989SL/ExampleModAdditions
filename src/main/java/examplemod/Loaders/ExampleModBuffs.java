package examplemod.Loaders;

import examplemod.examples.buffs.*;
import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class ExampleModBuffs {
        public static void load(){
            // Register our buff
            BuffRegistry.registerBuff("examplebuff", new ExampleBuff());

            // Register our Armor Set Bonus
            BuffRegistry.registerBuff("examplearmorsetbonusbuff", new ExampleArmorSetBuff());

            // Register our Arrow Buff
            BuffRegistry.registerBuff("examplearrowbuff", new ExampleArrowBuff());

            // Register our Trinket Buff
            BuffRegistry.registerBuff("exampletrinketbuff",new ExampleTrinketBuff());
        }
}
