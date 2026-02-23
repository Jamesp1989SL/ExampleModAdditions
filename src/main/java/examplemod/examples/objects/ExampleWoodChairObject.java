package examplemod.examples.objects;

import necesse.level.gameObject.furniture.ChairObject;

import java.awt.*;

public class ExampleWoodChairObject extends ChairObject {

    public ExampleWoodChairObject(){
        super("examplechair", new Color(116, 69, 43));

        // We set the category that this object should be part of
        // You can see the registered category stringIDs in the ItemCategory class. Link:
        /// {@link necesse.inventory.item.ItemCategory}

        // Often when you extend an existing object (like ChairObject in this case), it will have the categories
        // defined in that parent class. But in this case we want to use the category that we have defined
        // in ExampleModCategories
        setItemCategory("examplemod", "objects", "examplewood");
        // Same with crafting category (where they are displayed in the workstation)
        setCraftingCategory("examplemod", "objects", "examplewood");
    }

}
