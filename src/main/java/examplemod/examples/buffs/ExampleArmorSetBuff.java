package examplemod.examples.buffs;

import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.setBonusBuffs.SimpleSetBonusBuff;

/**
 * Set bonus buff:
 * When a player wears the full armor set, this buff is applied.
 * It gives +10% damage and +10% movement speed.
 */
public class ExampleArmorSetBuff extends SimpleSetBonusBuff {

    public ExampleArmorSetBuff() {
        // The parent class (SimpleSetBonusBuff) takes the stat boosts here.
        super(
                new ModifierValue<>(BuffModifiers.ALL_DAMAGE, 0.10f), // +10% damage
                new ModifierValue<>(BuffModifiers.SPEED, 0.10f)       // +10% speed
        );
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        // Set bonuses should not be removable by the player.
        this.canCancel = false;

        // Mark it as a passive buff (always active while wearing the set).
        this.isPassive = true;

        // Show it in the buff UI.
        this.isVisible = true;

        // Let the parent class finish setup (applies the modifiers).
        super.init(buff, eventSubscriber);
    }
}