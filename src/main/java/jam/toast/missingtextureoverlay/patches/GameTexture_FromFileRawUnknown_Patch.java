package jam.toast.missingtextureoverlay.patches;

import java.io.FileNotFoundException;

import jam.toast.missingtextureoverlay.MissingTextureTracker;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.gameTexture.GameTexture;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(
        target = GameTexture.class,
        name = "fromFileRawUnknown",
        arguments = { String.class }
)
public class GameTexture_FromFileRawUnknown_Patch {

    @Advice.OnMethodExit(onThrowable = FileNotFoundException.class)
    public static void onExit(
            @Advice.Argument(0) String filePath,
            @Advice.Thrown FileNotFoundException ex
    ) {
        // fromFileRawUnknown only throws when the file cannot be found/read.
        MissingTextureTracker.reportMissingTexture(filePath);
    }
}
