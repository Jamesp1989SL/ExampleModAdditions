package examplemod.examples.objects;

import necesse.level.gameObject.TreeSaplingObject;

public class ExampleTreeSaplingObject extends TreeSaplingObject {

    public ExampleTreeSaplingObject(){
        // Add To Any Sapling Global Ingrediant
        super("examplesapling",      // Texture Name
                "exampletree",                  // Resulting Object String ID
                1800,                           // Min Grow Time In Seconds
                2700,                           // Max Grow Time In Seconds
                true);                          // Add To Any Sapling Global Ingrediant

    }

}
