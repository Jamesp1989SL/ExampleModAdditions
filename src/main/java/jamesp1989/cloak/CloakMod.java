package jamesp1989.cloak;

import jamesp1989.cloak.buffs.CloakActiveBuff;
import jamesp1989.cloak.buffs.CloakCooldownBuff;
import jamesp1989.cloak.buffs.CloakTrinketBuff;
import jamesp1989.cloak.items.CloakTrinketItem;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ItemRegistry;

@ModEntry
public class CloakMod {
    public static final String CLOAK_TRINKET_BUFF = "cloaktrinket";
    public static final String CLOAK_ACTIVE = "cloakactive";
    public static final String CLOAK_COOLDOWN = "cloakcooldown";

    public void init() {
        BuffRegistry.registerBuff(CLOAK_TRINKET_BUFF, new CloakTrinketBuff());
        BuffRegistry.registerBuff(CLOAK_ACTIVE, new CloakActiveBuff());
        BuffRegistry.registerBuff(CLOAK_COOLDOWN, new CloakCooldownBuff());

        ItemRegistry.registerItem("cloaktrinket", new CloakTrinketItem(), 800.0F, true);
    }
}
