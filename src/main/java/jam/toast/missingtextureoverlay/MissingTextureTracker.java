package jam.toast.missingtextureoverlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import necesse.engine.GameLog;
import necesse.engine.window.WindowManager;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModLoader;
import necesse.gfx.gameFont.FontManager;
import necesse.gfx.gameFont.FontOptions;

/**
 * Stores missing texture events and renders an always-on-screen overlay.
 *
 * This is called from ByteBuddy patches injected into core game classes.
 */
public class MissingTextureTracker {
    private static final int MAX_VISIBLE = 18;

    private static final Map<String, Entry> missing = new ConcurrentHashMap<>();

    private static class Entry {
        final String filePath;
        final String modId;
        volatile int count;
        volatile long firstTimeMs;
        volatile long lastTimeMs;

        Entry(String modId, String filePath, long nowMs) {
            this.modId = modId;
            this.filePath = filePath;
            this.count = 1;
            this.firstTimeMs = nowMs;
            this.lastTimeMs = nowMs;
        }
    }

    /**
     * Called when a texture fails to load.
     */
    public static void reportMissingTexture(String filePath) {
        long now = System.currentTimeMillis();
        String modId = resolveRequestingModId();
        String key = modId + "|" + filePath;

        Entry entry = missing.compute(key, (k, existing) -> {
            if (existing == null) {
                return new Entry(modId, filePath, now);
            }
            existing.count += 1;
            existing.lastTimeMs = now;
            return existing;
        });

        // Log the first time we see a specific missing texture for a specific mod.
        if (entry != null && entry.count == 1) {
            GameLog.warn.println("[MissingTextureOverlay] Missing texture: " + filePath + " (requested by mod: " + modId + ")");
        }
    }

    private static String resolveRequestingModId() {
        try {
            LoadedMod running = LoadedMod.getRunningMod();
            if (running != null && running.getModSaveInfo() != null) {
                return running.getModSaveInfo().id;
            }

            ArrayList<LoadedMod> responsible = ModLoader.getResponsibleMods(Collections.singletonList(new Throwable()), false);
            if (responsible != null && !responsible.isEmpty() && responsible.get(0).getModSaveInfo() != null) {
                return responsible.get(0).getModSaveInfo().id;
            }
        } catch (Throwable ignored) {
        }
        return "unknown";
    }

    /**
     * Draws an overlay listing missing textures.
     *
     * Called from HUD.draw every frame.
     */
    public static void drawOverlay() {
        if (missing.isEmpty()) return;

        FontOptions header = new FontOptions(16).color(Color.RED).outline();
        FontOptions line = new FontOptions(12).color(Color.WHITE).outline();

        int x = 6;
        int y = 6;

        FontManager.bit.drawString(x, y, "MISSING TEXTURES", header);
        y += FontManager.bit.getHeightCeil("A", header) + 2;

        List<Entry> entries = new ArrayList<>(missing.values());
        entries.sort(Comparator.comparingLong((Entry e) -> e.lastTimeMs).reversed());

        int shown = 0;
        for (Entry e : entries) {
            if (shown >= MAX_VISIBLE) break;
            String text = "[" + e.modId + "] " + e.filePath + (e.count > 1 ? "  x" + e.count : "");

            // Clamp long lines to the hud width so it doesn't run forever.
            int maxPx = Math.max(200, WindowManager.getWindow().getHudWidth() - x - 6);
            text = clampToWidth(text, line, maxPx);

            FontManager.bit.drawString(x, y, text, line);
            y += FontManager.bit.getHeightCeil("A", line) + 1;
            shown++;
        }

        if (entries.size() > MAX_VISIBLE) {
            String more = "... +" + (entries.size() - MAX_VISIBLE) + " more";
            FontManager.bit.drawString(x, y, more, line);
        }
    }

    private static String clampToWidth(String text, FontOptions opts, int maxWidthPx) {
        if (FontManager.bit.getWidthCeil(text, opts) <= maxWidthPx) return text;

        String suffix = "...";
        int suffixW = FontManager.bit.getWidthCeil(suffix, opts);
        if (suffixW >= maxWidthPx) return suffix;

        // Binary-ish trim.
        int lo = 0;
        int hi = text.length();
        while (lo + 1 < hi) {
            int mid = (lo + hi) / 2;
            String candidate = text.substring(0, mid) + suffix;
            int w = FontManager.bit.getWidthCeil(candidate, opts);
            if (w <= maxWidthPx) lo = mid; else hi = mid;
        }
        return text.substring(0, lo) + suffix;
    }
}
