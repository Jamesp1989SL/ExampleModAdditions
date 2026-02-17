package examplemod.examples.items.materials;

import necesse.inventory.item.matItem.MatItem;

public class ExampleLogItem extends MatItem {

    public ExampleLogItem() {
        super(500,                  // Max Stack Size
                Rarity.UNCOMMON,             // Rarity
                new String[]{"anylog"});     // Global Ingrediants

    }
}
