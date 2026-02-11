package examplemod.examples.buffs;

import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

/**
 * ExampleArrowBuff
 *
 * A server-side buff that periodically heals the mob it is applied to.
 *
 * Key points:
 * - The healing is done on the SERVER in {@link #serverTick(ActiveBuff)} so it is authoritative in multiplayer.
 * - The buff uses "gndData" (Game Network Data) to store per-instance state:
 *     - "healPerTick": how much health to add each heal tick (must be set by whoever applies the buff)
 *     - "timePassed": internal timer accumulator to control the heal interval
 * - A {@link MobHealthChangeEvent} is spawned so the game can display/hear the heal change properly
 *   (numbers, effects, syncing, etc.), rather than silently changing health.
 */
public class ExampleArrowBuff extends Buff {

    /**
     * How often we apply healing (in milliseconds).
     * We simulate this by accumulating time in server ticks (see {@link #serverTick(ActiveBuff)}).
     */
    private static final int HEAL_INTERVAL_MS = 250;

    public ExampleArrowBuff() {
        // Player can't right-click/cancel the buff
        this.canCancel = false;

        // Hide from the UI (no buff icon)
        this.isVisible = false;

        // Not a passive stat modifier style buff
        this.isPassive = false;

        // Don't save this buff to disk (intended as temporary / combat effect)
        this.shouldSave = false;
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        // Initialize our per-buff timer accumulator.
        // This value lives in the buff instance's gndData so each mob/buff has its own timer.
        buff.getGndData().setInt("timePassed", 0);

        // NOTE: We intentionally do NOT set "healPerTick" here.
        // Whoever applies the buff should set it, e.g.:
        //   buff.getGndData().setInt("healPerTick", 2);
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        // The mob that currently has this buff.
        Mob m = buff.owner;
        if (m == null) return;

        // How much to heal each time we trigger a heal tick.
        // If not set, it will default to 0, meaning "do nothing".
        int heal = buff.getGndData().getInt("healPerTick");
        if (heal <= 0) return;

        /*
         * Necesse serverTick runs at a fixed tick rate.
         * In this example we assume 1 server tick ≈ 50ms and accumulate time ourselves.
         *
         * (If tick rate changes, you'd want to accumulate using the actual delta time if available,
         *  but for many simple buffs, a fixed step like this is fine.)
         */
        int accum = buff.getGndData().getInt("timePassed");
        accum += 50;

        // Not enough time has passed yet: store accumulator and wait for the next tick.
        if (accum < HEAL_INTERVAL_MS) {
            buff.getGndData().setInt("timePassed", accum);
            return;
        }

        // Enough time passed: "spend" one interval worth of time.
        // We subtract rather than zeroing so leftover time isn't lost.
        accum -= HEAL_INTERVAL_MS;
        buff.getGndData().setInt("timePassed", accum);

        // Compute how much healing we can actually apply without exceeding max health.
        int before = m.getHealth();
        int finalHealth = Math.min(m.getMaxHealth(), before + heal);
        int applied = finalHealth - before;

        // If healing would have no effect (already full), do nothing.
        if (applied > 0) {
            // Spawn a health change event instead of directly forcing health,
            // so the engine can properly sync and display the heal.
            m.getLevel().entityManager.events.add(new MobHealthChangeEvent(m, finalHealth, applied));
        }
    }
}
