package colourRGB.UI;

import necesse.engine.network.client.Client;
import necesse.gfx.forms.components.FormColorPicker;
import necesse.gfx.forms.presets.containerComponent.ContainerForm;
import colourRGB.Containers.RGBLEDPanelContainer;

import java.awt.Color;

public class RGBLEDPanelContainerForm extends ContainerForm<RGBLEDPanelContainer> {

    public RGBLEDPanelContainerForm(Client client, RGBLEDPanelContainer container) {
        super(client, 220, 210, container);

        FormColorPicker picker = addComponent(new FormColorPicker(10, 10, 200, 170));
        picker.setSelectedColor(container.oe.getColor());

        // IMPORTANT: FormColorPicker uses a listener, not an override
        picker.onChanged(e -> {
            Color col = picker.getSelectedColor();
            if (col == null) return;

            int rgb = ((col.getRed() & 0xFF) << 16) |
                    ((col.getGreen() & 0xFF) << 8) |
                    (col.getBlue() & 0xFF);

            container.setColorAction.runAndSend(rgb);
        });
    }
}
