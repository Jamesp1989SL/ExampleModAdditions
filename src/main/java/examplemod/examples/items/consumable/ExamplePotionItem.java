package examplemod.examples.items.consumable;

import necesse.inventory.item.placeableItem.consumableItem.potionConsumableItem.SimplePotionItem;

public class ExamplePotionItem extends SimplePotionItem {

    public ExamplePotionItem() {
        super(100,                           // Max Stack Size
                Rarity.COMMON,                        // Item Rarity
                "examplebuff",                        // Buff to apply
                100,                                  // Buff Duration in seconds
                "examplepotionitemtip");   // Localization text to load and display
    }

}