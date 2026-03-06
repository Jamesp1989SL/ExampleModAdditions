package jamesp1989.cloak.buffs;

import jamesp1989.cloak.CloakMod;
import jamesp1989.cloak.buffs.base.BaseCloakAbilityTrinketBuff;

public class CloakTrinketBuff extends BaseCloakAbilityTrinketBuff {
    @Override
    protected String getActiveBuffStringID() {
        return CloakMod.CLOAK_ACTIVE;
    }

    @Override
    protected String getCooldownBuffStringID() {
        return CloakMod.CLOAK_COOLDOWN;
    }

    @Override
    protected float getRechargeSeconds() {
        return 12.0F;
    }

}
