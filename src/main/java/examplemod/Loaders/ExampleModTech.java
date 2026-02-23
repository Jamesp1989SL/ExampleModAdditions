package examplemod.Loaders;

import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Tech;

public class ExampleModTech {

        public static Tech EXAMPLE_TECH;

        public static void load(){
                // stringID: how recipes refer to it internally
                // itemStringID: used for icon/tooltips (usually your crafting station item id)
                EXAMPLE_TECH = RecipeTechRegistry.registerTech("exampletech", "examplecraftingstation");

                // Remember to also add the tech to your locale file. The name of the tech will be
                // shown in the crafting guide book, etc. (This is already done in this example)
        }

}
