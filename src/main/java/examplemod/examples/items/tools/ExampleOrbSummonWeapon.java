package examplemod.examples.items.tools;

import necesse.entity.mobs.itemAttacker.FollowPosition;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.summonToolItem.SummonToolItem;
import necesse.inventory.lootTable.presets.SummonWeaponsLootTable;

public class ExampleOrbSummonWeapon extends SummonToolItem {
    public ExampleOrbSummonWeapon() {
        // , followPosition, summonSpaceTaken, enchantCost, lootTableCategory
        super("examplesummonmob",               // Mob String ID
                FollowPosition.PYRAMID,                    // Follow Position
                1.0F,                                      // Summon Space Taken
                400,                                       // Enchant Cost
                SummonWeaponsLootTable.summonWeapons);     // Loot Table Category

        this.rarity = Item.Rarity.UNCOMMON;

        // This damage is what gets injected into your minion via mob.updateDamage(getAttackDamage(item))
        this.attackDamage.setBaseValue(50.0F).setUpgradedValue(1.0F, 45.0F);

        // Offset the X location of the attack texture
        this.attackXOffset = 15;
        // Offset the X location of the attack texture
        this.attackYOffset = 10;


    }
}