package jamesp1989.cloak.buffs.base;

import necesse.engine.network.Packet;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffAbility;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.TrinketBuff;

public abstract class BaseCloakAbilityTrinketBuff extends TrinketBuff implements BuffAbility {
    public static final String COOLDOWN_GND_KEY = "cloakcooldownSeconds";

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
    }

    protected abstract String getActiveBuffStringID();

    protected abstract String getCooldownBuffStringID();

    protected float getRechargeSeconds() {
        return 11.0F;
    }


    @Override
    public void runAbility(PlayerMob player, ActiveBuff buff, Packet content) {
        if (!canRunAbility(player, buff, content)) {
            return;
        }

        ActiveBuff activeBuff = new ActiveBuff(
                BuffRegistry.getBuff(getActiveBuffStringID()),
                player,
                1.0f,
                null
        );

        GNDItemMap gndData = activeBuff.getGndData();
        gndData.setFloat(COOLDOWN_GND_KEY, getRechargeSeconds());
        activeBuff.setGndData(gndData);

        player.buffManager.addBuff(activeBuff, false);
        player.buffManager.forceUpdateBuffs();
    }

    @Override
    public boolean canRunAbility(PlayerMob player, ActiveBuff buff, Packet content) {
        return !player.buffManager.hasBuff(getActiveBuffStringID())
                && !player.buffManager.hasBuff(getCooldownBuffStringID());
    }

    @Override
    public void onRemoved(ActiveBuff buff) {
        super.onRemoved(buff);

        if (buff.owner != null && buff.owner.isPlayer) {
            buff.owner.buffManager.removeBuff(getActiveBuffStringID(), false);
            buff.owner.buffManager.forceUpdateBuffs();
        }
    }
}
