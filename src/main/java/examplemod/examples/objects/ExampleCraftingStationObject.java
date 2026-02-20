package examplemod.examples.objects;

import examplemod.Loaders.ExampleModTech;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.recipe.Tech;
import necesse.level.gameObject.container.CraftingStationObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.util.List;

//TODO add rendering code
public class ExampleCraftingStationObject extends CraftingStationObject {
    // Loaded once from mod resources in loadTextures()
    private GameTexture texture;
    
    public ExampleCraftingStationObject() {
        super();
        // Optional: set categories like vanilla crafting stations do
        //setItemCategory("objects", "craftingstations");
        //setCraftingCategory("craftingstations");
    }

    @Override
    public void loadTextures() {
        super.loadTextures();

        // Loads: src/main/resources/objects/exampleleveleventobject.png
        // (no ".png" in the string)
        this.texture = GameTexture.fromFile("objects/examplejobobject");
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY, TickManager tickManager,
                             GameCamera camera, PlayerMob perspective) {

        // Match sprite lighting to the level light at this tile
        GameLight light = level.getLightLevel(tileX, tileY);

        // Convert tile coordinates to screen draw coordinates
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Build draw options once (sprite + lighting + position)
        final TextureDrawOptionsEnd opts = this.texture.initDraw()
                .sprite(0, 0, 32)     // sprite index (0,0), size 32
                .light(light)
                .pos(drawX, drawY);

        /*
         */
        tileList.add(tm -> opts.draw());
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha,
                            PlayerMob player, GameCamera camera) {

        // Placement preview ("ghost" sprite) while holding the item
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        this.texture.initDraw()
                .sprite(0, 0, 32)
                .light(light)
                .alpha(alpha)
                .draw(drawX, drawY);
    }

    @Override
    public Tech[] getCraftingTechs() {
        // Assign what tech/s the station will use
        return new Tech[] { ExampleModTech.EXAMPLE_TECH };
    }
}