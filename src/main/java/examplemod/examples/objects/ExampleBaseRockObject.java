package examplemod.examples.objects;
import necesse.level.gameObject.RockObject;

import java.awt.Color;

public class ExampleBaseRockObject extends RockObject {

    public ExampleBaseRockObject() {
        super("examplebaserock",            // Texture for the base rock
                new Color(92, 37, 23),        // Mini Map Pixel Colour
                "examplestone",                        // Dropped Stone
                "objects", "landscaping");   // Item Categories

        this.toolTier = 0.0F;                          // Tier of pickaxe required to mine this rock
    }
}
