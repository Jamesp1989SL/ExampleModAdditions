package jamesp1989.cloak.buffs;

import jamesp1989.cloak.CloakMod;
import jamesp1989.cloak.buffs.base.BaseCloakActiveBuff;

public class CloakActiveBuff extends BaseCloakActiveBuff {

    @Override
    protected String getActiveBuffStringID() {
        return CloakMod.CLOAK_ACTIVE;
    }

    @Override
    protected String getCooldownBuffStringID() {
        return CloakMod.CLOAK_COOLDOWN;
    }

    @Override
    protected float getDefaultCooldownSeconds() {
        return 12.0F;
    }

    @Override
    protected float getAllDamageBonus() {
        return 5.0F;
    }

    @Override
    protected boolean cancelOnDamage() {
        return true;
    }

}