package examplemod.Loaders;

import necesse.engine.localization.message.LocalMessage;
import necesse.inventory.item.ItemCategory;

public final class ExampleModCategories {
    private ExampleModCategories() {}

    /*
     * IMPORTANT (Creative Menu requirement)
     * ------------------------------------
     * The Creative menu tabs currently only browse a small set of hard-coded ROOT categories but this will be changed in the future.
     *
     * Placeables tab roots: tiles / objects / wiring
     * Items tab roots:      equipment / consumable / materials / misc
     * Mobs tab roots:       mobs
     *
     * So: your itemCategoryTree MUST start with one of those roots, otherwise the item/object
     * will not appear in Creative even though it is registered.
     * Again, this will be changed in the next update (1.2.0) where all categories will be shown by default in the items tab.
     */

    public static void load() {
        // ITEM CATEGORIES
        ItemCategory.createCategory(
                "Z-EXAMPLEMOD",
                new LocalMessage("itemcategory", "examplemodrootcat"),
                "examplemod"
        );

        ItemCategory.createCategory(
                "Z-EXAMPLEMOD-OBJECTS",
                new LocalMessage("itemcategory", "examplemodobjectsubcat"),
                "examplemod", "objects"
        );

        ItemCategory.createCategory(
                "Z-EXAMPLEMOD-OBJECTS-FURNITURE",
                new LocalMessage("itemcategory", "examplemodfurnaturesubcat"),
                "examplemod", "objects", "examplewood"
        );

        // CRAFTING CATEGORIES
        // These categories are used in workstations. They define the order in which the categories are shown
        ItemCategory.craftingManager.createCategory(
                "Z-EXAMPLEMOD",
                new LocalMessage("itemcategory", "examplemodrootcat"),
                "examplemod"
        );

        ItemCategory.craftingManager.createCategory(
                "Z-EXAMPLEMOD-OBJECTS",
                new LocalMessage("itemcategory", "examplemodobjectsubcat"),
                "examplemod", "objects"
        );

        ItemCategory.craftingManager.createCategory(
                "Z-EXAMPLEMOD-OBJECTS-FURNATURE",
                new LocalMessage("itemcategory", "examplemodfurnaturesubcat"),
                "examplemod", "objects", "examplewood"
        );

        // In
    }
}
