package colourRGB;

import colourRGB.Containers.RGBLEDPanelContainer;
import colourRGB.Objects.RGBLEDPanelObject;
import colourRGB.Objects.RGBLEDPanelObjectEntity;
import colourRGB.UI.RGBLEDPanelContainerForm;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.registries.ObjectRegistry;


@ModEntry
public class ColourRGB {

    public void init() {

        RGBLEDPanelObject.RGB_LED_PANEL_CONTAINER = ContainerRegistry.registerOEContainer(
                (client, uniqueSeed, oe, content) -> {
                    RGBLEDPanelObjectEntity rgbOE = (RGBLEDPanelObjectEntity) oe;
                    return new RGBLEDPanelContainerForm(
                            client,
                            new RGBLEDPanelContainer(client.getClient(), uniqueSeed, rgbOE)
                    );
                },
                (client, uniqueSeed, oe, content, serverObject) -> new RGBLEDPanelContainer(client, uniqueSeed, (RGBLEDPanelObjectEntity) oe)
        );
        RGBLEDPanelObject rgbobject = new RGBLEDPanelObject();
        ObjectRegistry.registerObject("colourledpanel", rgbobject, -1.0F, true);
    }

    public void initResources() {
    }

    public void postInit() {

    }
}
