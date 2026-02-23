package examplemod.examples.objects;

import necesse.level.gameObject.TreeSaplingObject;

public class ExampleTreeSaplingObject extends TreeSaplingObject {

    public ExampleTreeSaplingObject(){
        super(
                "examplesapling", // Texture name
                "exampletree", // Grown object stringID
                30 * 60, // Min grow time in seconds - 30 minutes
                45 * 60, // Max grow time in seconds - 45 minutes
                true // Can be used as "Any sapling" ingredient
        );
    }

}
