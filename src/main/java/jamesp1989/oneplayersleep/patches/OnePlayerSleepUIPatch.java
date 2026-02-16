package jamesp1989.oneplayersleep.patches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.client.Client;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.forms.presets.containerComponent.SleepContainerForm;
import necesse.inventory.container.BedContainer;
import net.bytebuddy.asm.Advice;

import java.awt.Rectangle;

@ModMethodPatch(
        target = SleepContainerForm.class,
        name = "draw",
        arguments = { TickManager.class, PlayerMob.class, Rectangle.class }
)
public class OnePlayerSleepUIPatch {

    @Advice.OnMethodEnter
    public static void onEnter(
            @Advice.This SleepContainerForm self,
            @Advice.Local("oldSleepingPlayers") int oldSleepingPlayers,
            @Advice.Local("didSpoof") boolean didSpoof
    ) {
        if (self == null) return;

        BedContainer container = self.getContainer();
        Client client = self.getClient();
        if (container == null || client == null) return;

        // Only bother if at least 1 is sleeping but not everyone
        long totalL = client.streamClients().count();
        int total = (totalL > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) totalL;

        if (container.sleepingPlayers > 0 && container.sleepingPlayers < total) {
            oldSleepingPlayers = container.sleepingPlayers;
            container.sleepingPlayers = total; // forces "not waiting" branch
            didSpoof = true;
        }
    }

    @Advice.OnMethodExit
    public static void onExit(
            @Advice.This SleepContainerForm self,
            @Advice.Local("oldSleepingPlayers") int oldSleepingPlayers,
            @Advice.Local("didSpoof") boolean didSpoof
    ) {
        if (!didSpoof || self == null) return;

        BedContainer container = self.getContainer();
        if (container != null) {
            container.sleepingPlayers = oldSleepingPlayers; // restore real value
        }
    }
}