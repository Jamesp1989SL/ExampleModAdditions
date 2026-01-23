package jam.toast.missingtextureoverlay;

import necesse.engine.modLoader.annotations.ModEntry;

/**
 * Empty entrypoint: this mod is primarily ByteBuddy patches.
 * Keeping an entrypoint makes the mod easy to identify and enables future config.
 */
@ModEntry
public class MissingTextureOverlayMod {
    public void init() {
        // No-op
    }
}
