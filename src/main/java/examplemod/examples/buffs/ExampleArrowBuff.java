package examplemod.examples.buffs;

import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class ExampleArrowBuff extends Buff {

    // Heal every 250ms (about 1/4 second)
    private static final int HEAL_INTERVAL_MS = 250;

    public ExampleArrowBuff() {
        // Keep this buff hidden + temporary
        this.canCancel = false;
        this.isVisible = false;
        this.isPassive = false;
        this.shouldSave = false;
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        // Timer for this buff instance
        buff.getGndData().setInt("timePassed", 0);

        // "healPerTick" is set by whoever applies the buff
        // buff.getGndData().setInt("healPerTick", 2);
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        Mob mob = buff.owner;
        if (mob == null) return;

        // How much to heal each time (0 = no healing)
        int healPerTick = buff.getGndData().getInt("healPerTick");
        if (healPerTick <= 0) return;

        // Add ~50ms per server tick
        int time = buff.getGndData().getInt("timePassed") + 50;

        // Not ready to heal yet
        if (time < HEAL_INTERVAL_MS) {
            buff.getGndData().setInt("timePassed", time);
            return;
        }

        // Ready: keep leftover time and heal once
        buff.getGndData().setInt("timePassed", time - HEAL_INTERVAL_MS);

        // Heal, but don't go past max health
        int newHealth = Math.min(mob.getMaxHealth(), mob.getHealth() + healPerTick);

        // If health actually changed, tell the game with an event (sync + floating numbers)
        if (newHealth != mob.getHealth()) {
            int amountHealed = newHealth - mob.getHealth();
            mob.getLevel().entityManager.events.add(
                    new MobHealthChangeEvent(mob, newHealth, amountHealed)
            );
        }
    }
}