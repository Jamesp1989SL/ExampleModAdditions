package colourRGB.Objects;

import necesse.engine.network.client.Client;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.networkField.IntNetworkField;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.Color;

public class RGBLEDPanelObjectEntity extends ObjectEntity {

    // NEW: onChanged refreshes lighting + map tile on clients
    private final IntNetworkField packedRGB = registerNetworkField(new IntNetworkField(0xFFFFFF) {
        @Override
        public void onChanged(Integer value) {
            Level lvl = getLevel();
            if (lvl == null) return;

            // Update lighting immediately
            lvl.lightManager.updateStaticLight(tileX, tileY);

            // Update minimap/world-map tile colour on clients
            if (lvl.isClient() && lvl.getClient() != null) {
                Client client = lvl.getClient();
                client.levelManager.updateMapTile(tileX, tileY);
            }
        }
    });

    public RGBLEDPanelObjectEntity(Level level, int tileX, int tileY) {
        super(level, "rgbledpanel", tileX, tileY);
    }

    public Color getColor() {
        int rgb = packedRGB.get() & 0xFFFFFF;
        return new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
    }

    public void setPackedRGB(int rgb) {
        packedRGB.set(rgb & 0xFFFFFF);
        markDirty();
        // lighting + map updates happen in packedRGB.onChanged(...)
    }

    @Override
    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addInt("rgb", packedRGB.get() & 0xFFFFFF);
    }

    @Override
    public void applyLoadData(LoadData load) {
        super.applyLoadData(load);
        setPackedRGB(load.getInt("rgb", 0xFFFFFF));
    }
}