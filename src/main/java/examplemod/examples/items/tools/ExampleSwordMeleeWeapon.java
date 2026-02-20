package examplemod.examples.items.tools;

import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.swordToolItem.SwordToolItem;

// Extends SwordToolItem
public class ExampleSwordMeleeWeapon extends SwordToolItem {

    // Weapon attack textures are loaded from resources/player/weapons/<itemStringID>

    public ExampleSwordMeleeWeapon() {
        super(400,                                              // Enchant Cost
                null);                                                     // Loot Table Category (there isn't a built-in one for melee
        rarity = Item.Rarity.UNCOMMON;                                     // Rarity
        attackAnimTime.setBaseValue(300);                                  // 300 ms attack time
        attackDamage.setBaseValue(20)                                      // Base Sword damage
                .setUpgradedValue(1, 95);                        // Upgraded Tier 1 Damage
        attackRange.setBaseValue(120);                                     // 120 Range
        knockback.setBaseValue(100);                                       // 100 Knockback

    }

}
