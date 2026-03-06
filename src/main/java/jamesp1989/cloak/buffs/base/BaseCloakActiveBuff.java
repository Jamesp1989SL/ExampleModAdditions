package jamesp1989.cloak.buffs.base;


import necesse.engine.registries.BuffRegistry;
import necesse.entity.mobs.MobWasHitEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.HumanDrawBuff;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.drawOptions.human.HumanDrawOptions;


public abstract class BaseCloakActiveBuff extends Buff implements HumanDrawBuff {

    public BaseCloakActiveBuff() {
        this.canCancel = false;
        this.isImportant = true;
        this.isPassive = true;
        this.isVisible = false;
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        // Make normal targeting fail against the player
        buff.setModifier(BuffModifiers.UNTARGETABLE, true);

        // Reduce target search range against the player
        if (usesTargetRangeModifier()) {
            buff.setModifier(BuffModifiers.TARGET_RANGE, getTargetRangeModifier());
        }

        // Bonus damage while the cloak is active
        float damageBonus = getAllDamageBonus();
        if (damageBonus != 0.0F) {
            buff.setModifier(BuffModifiers.ALL_DAMAGE, damageBonus);
        }

        addExtraModifiers(buff, eventSubscriber);
    }

    @Override
    public void onHasAttacked(ActiveBuff buff, MobWasHitEvent event) {
        super.onHasAttacked(buff, event);

        if (event == null || event.target == null || event.wasPrevented) {
            return;
        }

        if (buff.owner == null || !buff.owner.isPlayer) {
            return;
        }

        breakCloak(buff);
    }

    protected void breakCloak(ActiveBuff buff) {
        float cooldownSeconds = getCooldownSeconds(buff);

        buff.owner.buffManager.removeBuff(getActiveBuffStringID(), true);
        buff.owner.buffManager.addBuff(
                new ActiveBuff(BuffRegistry.getBuff(getCooldownBuffStringID()), buff.owner, cooldownSeconds, null),
                true
        );
    }

    protected boolean cancelOnDamage() {
        return false;
    }

    @Override
    public void onWasHit(ActiveBuff buff, MobWasHitEvent event) {
        super.onWasHit(buff, event);

        if (!cancelOnDamage()) {
            return;
        }

        if (event == null || event.wasPrevented) {
            return;
        }

        if (buff.owner == null || !buff.owner.isPlayer) {
            return;
        }

        breakCloak(buff);
    }

    @Override
    public void addHumanDraw(ActiveBuff buff, HumanDrawOptions options) {
        // Make the player look faded while the cloak is active
        options.allAlpha(getPlayerAlpha());
    }

    protected boolean usesTargetRangeModifier() {
        return true;
    }

    protected float getTargetRangeModifier() {
        return -1.0F;
    }

    protected float getAllDamageBonus() {
        return 0.24F;
    }

    protected float getCooldownSeconds(ActiveBuff buff) {
        if (buff.getGndData().hasKey("cloakcooldownSeconds")) {
            return buff.getGndData().getFloat("cloakcooldownSeconds");
        }
        return getDefaultCooldownSeconds();
    }

    protected float getPlayerAlpha() {
        return 0.30F;
    }

    protected void addExtraModifiers(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
    }

    protected abstract String getActiveBuffStringID();

    protected abstract String getCooldownBuffStringID();

    protected abstract float getDefaultCooldownSeconds();
}