package examplemod.examples.items.trinkets;

import necesse.engine.localization.Localization;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.trinketItem.TrinketItem;
import necesse.inventory.lootTable.presets.TrinketsLootTable;

public class ExampleTrinketItem extends TrinketItem {

    // What buff this trinket gives when equipped
    private static final String BUFF_ID = "exampletrinketbuff";

    public ExampleTrinketItem() {
        // Basic trinket settings (rarity, enchant cost, loot group)
        super(Rarity.UNCOMMON, 400, TrinketsLootTable.trinkets);
    }

    @Override
    public TrinketBuff[] getBuffs(InventoryItem inventoryItem) {
        // Give the player our buff while the trinket is equipped
        return new TrinketBuff[] { (TrinketBuff) BuffRegistry.getBuff(BUFF_ID) };
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        // Start with normal tooltip, then add 1 extra line
        ListGameTooltips t = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        t.add(Localization.translate("itemtooltip", "exampletrinkettip"));
        return t;
    }
}