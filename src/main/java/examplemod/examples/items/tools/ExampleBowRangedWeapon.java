package examplemod.examples.items.tools;

import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.projectileToolItem.bowProjectileToolItem.BowProjectileToolItem;
import necesse.inventory.lootTable.presets.BowWeaponsLootTable;

public class ExampleBowRangedWeapon extends BowProjectileToolItem {
    public ExampleBowRangedWeapon() {
        // (enchantCost, lootTableCategory)
        super(100,                                  // Enchant Cost
                BowWeaponsLootTable.bowWeapons);               // Loot Table Category

        this.rarity = Item.Rarity.NORMAL;

        // Core stats
        this.attackAnimTime.setBaseValue(800);                 // Ms Per Shot
        this.attackDamage.setBaseValue(12.0F);                 // Base Bow Damage
        this.attackRange.setBaseValue(600);                    // Range In Tiles (Ish)
        this.velocity.setBaseValue(100);                       // Base Projectile Velocity
        this.knockback.setBaseValue(25);                       // Base Knockback

        // Sprite offsets (tune until it looks right in-hand)
        this.attackXOffset = 8;
        this.attackYOffset = 20;

        // How much the bow sprite “stretches” while charging
        this.attackSpriteStretch = 4;

        // Optional
        this.canBeUsedForRaids = true;
    }
}
