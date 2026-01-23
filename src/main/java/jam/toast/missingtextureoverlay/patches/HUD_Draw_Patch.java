package jam.toast.missingtextureoverlay.patches;

import jam.toast.missingtextureoverlay.MissingTextureTracker;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.ui.HUD;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(
        target = HUD.class,
        name = "draw",
        arguments = { Level.class, GameCamera.class, PlayerMob.class, TickManager.class }
)
public class HUD_Draw_Patch {

    @Advice.OnMethodExit
    public static void onExit() {
        // Always visible (not dev-only). The tracker decides whether anything needs to be drawn.
        MissingTextureTracker.drawOverlay();
    }
}
