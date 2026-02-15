package examplemod.examples.objects;

import java.awt.Color;

import necesse.level.gameObject.MaskedPressurePlateObject;

public class ExamplePressurePlateObject extends MaskedPressurePlateObject {

    public ExamplePressurePlateObject() {
        // Map color used on the minimap.
        super("pressureplatemask","exampletile",new Color(120, 80, 200));

        // MaskedPressurePlateObject sets the important flags internally (including isPressurePlate)
        // and uses a default trigger hitbox through its object entity.
    }
}