package examplemod.examples.objects;
import necesse.level.gameObject.RockObject;
import necesse.level.gameObject.RockOreObject;

import java.awt.Color;

/**
 * Example ore rock that uses our ExampleIncursionDeepRockObject as its parent rock.
 */
public class ExampleOreRockObject extends RockOreObject {

    public ExampleOreRockObject(RockObject parentRock) {

        super(parentRock,
                "oremask",              // Ore Mask Image
                "exampleore",                             // Ore Texture Name
                new Color(90, 40, 160),          // Mini Map Color
                "exampleore",                             // Dropped Ore
                1,                                        // Min Drop
                3,                                        // Max Drop
                2,                                        // Placed Dropped Ore
                true,                                     // Is Incrustion Extraction Object
                "objects", "landscaping");      // Categories
    }
}
