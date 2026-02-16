package jamesp1989.oneplayersleep.patches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.inventory.container.BedContainer;
import necesse.inventory.container.Container;
import net.bytebuddy.asm.Advice;

import java.util.Iterator;

@ModMethodPatch(
        target = BedContainer.class,
        name = "tick",
        arguments = {}
)
public class OnePlayerSleepPatch {

    @Advice.OnMethodExit
    public static void onExit(@Advice.This BedContainer self) {
        if (self == null || !self.getClient().isServer()) return;

        // Match vanilla: only after the initial "lying down" wait
        if (self.sleepTimer <= BedContainer.WAIT_TIME) return;

        Server server = self.getClient().getServerClient().getServer();

        boolean anySleeping = false;

        Iterator<ServerClient> it = server.streamClients().iterator();
        while (it.hasNext()) {
            ServerClient c = it.next();
            Container cont = c.getContainer();

            if (cont instanceof BedContainer) {
                BedContainer bc = (BedContainer) cont;
                if (bc.sleepTimer > BedContainer.WAIT_TIME) {
                    anySleeping = true;
                    break;
                }
            }
        }

        // If anyone is sleeping, pretend "everyone is sleeping"
        // This removes the "Waiting for players" UI because the UI checks sleepingPlayers vs total clients.
        if (anySleeping) {
            long total = server.streamClients().count();
            if (total > Integer.MAX_VALUE) total = Integer.MAX_VALUE;
            self.sleepingPlayers = (int) total;
        }

        // Wake time logic (same as before)
        if (self.nextWakeUpTime == 0L && anySleeping) {
            float morning = server.world.worldEntity.hourToDayTime(7.0F);
            long toMorning = server.world.getTimeToNextTimeOfDay((int) morning);

            if (toMorning < 30000L) {
                toMorning += server.world.worldEntity.getDayTimeMax() * 1000L;
            }

            self.nextWakeUpTime = server.world.getWorldTime() + toMorning;
        } else if (!anySleeping) {
            self.nextWakeUpTime = 0L;
        }

        if (anySleeping) {
            server.world.worldEntity.keepSleeping();
        }
    }
}