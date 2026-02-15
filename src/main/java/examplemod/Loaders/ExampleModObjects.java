package examplemod.Loaders;

import examplemod.examples.objects.*;
import necesse.engine.registries.ObjectRegistry;
import necesse.level.gameObject.WallObject;

//NOTE item and crafting categories subject to change
public class ExampleModObjects {

    // Expose IDs for other classes (biomes, levels, etc.)
    public static int EXAMPLE_BASE_ROCK_ID = -1;
    public static int EXAMPLE_ORE_ROCK_ID  = -1;

    public static void load(){
        // Register our objects

        ObjectRegistry.registerObject("exampleobject", new ExampleObject()
                .setItemCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS)
                .setCraftingCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS), 2, true);


        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        EXAMPLE_BASE_ROCK_ID = ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1.0F, true);

        // Register an ore rock object that overlays onto our incursion rock
        EXAMPLE_ORE_ROCK_ID = ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register a wall object, window object and door object
        ExampleWallWindowDoorObject.registerWallsDoorsWindows();

        // Register a tree object
        ObjectRegistry.registerObject("exampletree",new ExampleTreeObject(),0.0F,false,false,false);

        // Register a sapling object
        ObjectRegistry.registerObject("examplesapling", new ExampleTreeSaplingObject(),10,true);

        // Register a furnature object this won't currently display in creative due to how creative is coded but this is subject to change
        ObjectRegistry.registerObject("examplechair", new ExampleWoodChairObject()
                .setItemCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS,ExampleModCategories.EXAMPLEWOOD)
                .setCraftingCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS,ExampleModCategories.EXAMPLEWOOD),50,true);

        // Register a grass object
        ObjectRegistry.registerObject("examplegrass",new ExampleGrassObject(),1,true);

        // Register an object which uses a level event
        ObjectRegistry.registerObject("exampleleveleventobject", new ExampleLevelEventObject(),1,true);

        // Register ExampleJobObject an object that triggers our new job to happen //DEBUG
        ObjectRegistry.registerObject("examplejobobject",new ExampleJobObject(),1,true);

        // Register an object that uses the mods config file
        ObjectRegistry.registerObject("exampleconfigobject", new ExampleConfigObject(),1,true);

        // Register an example pressure plate object
        ObjectRegistry.registerObject("examplepressureplateobject",new ExamplePressurePlateObject(),1,true);

        // Get the wall object we want this trap to attach to.
        // ObjectRegistry stores everything as a generic "GameObject"
        // so we fetch by string ID ("examplewall") and cast it to WallObject.
        // Takes the texture of the wall object and overlays our "examplewalltrapobject"
        WallObject exampleWall = (WallObject) ObjectRegistry.getObject("examplewall");
        ObjectRegistry.registerObject("examplewalltrapobject",new ExampleWallTrapObject(exampleWall),1,true);



    }
}
