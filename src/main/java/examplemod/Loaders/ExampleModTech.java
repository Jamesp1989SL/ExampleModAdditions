package examplemod.Loaders;

import examplemod.examples.buffs.ExampleArmorSetBuff;
import examplemod.examples.buffs.ExampleArrowBuff;
import examplemod.examples.buffs.ExampleBuff;
import examplemod.examples.buffs.ExampleTrinketBuff;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Tech;

public class ExampleModTech {
        public static Tech EXAMPLE_TECH;

        public static void load(){
                // stringID: how recipes refer to it internally
                // itemStringID: used for icon/tooltips (usually your crafting station item id)
                EXAMPLE_TECH = RecipeTechRegistry.registerTech("exampletech", "examplecraftingstation");
        }
}
