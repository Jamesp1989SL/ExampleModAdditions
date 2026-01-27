package colourRGB.Containers;

import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.level.maps.Level;
import colourRGB.Objects.RGBLEDPanelObjectEntity;

public class RGBLEDPanelContainer extends Container {
    public final RGBLEDPanelObjectEntity oe;

    public final IntCustomAction setColorAction;

    public RGBLEDPanelContainer(NetworkClient client, int uniqueSeed, RGBLEDPanelObjectEntity oe) {
        super(client, uniqueSeed);
        this.oe = oe;

        this.setColorAction = registerAction(new IntCustomAction() {
            @Override
            protected void run(int rgb) {
                oe.setPackedRGB(rgb);
            }
        });
    }

    @Override
    public boolean isValid(ServerClient client) {
        if (!super.isValid(client)) return false;

        Level level = client.getLevel();
        if (oe.removed()) return false;

        return level.getObject(oe.tileX, oe.tileY)
                .isInInteractRange(level, oe.tileX, oe.tileY, client.playerMob);
    }
}