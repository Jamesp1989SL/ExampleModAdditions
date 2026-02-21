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

//TODO figure out item art

import java.awt.*;
import java.util.List;

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
        this.texture = GameTexture.fromFile("objects/examplecraftingstation");
    }

    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {
        if (rotation % 2 == 0)
            return new Rectangle(x * 32 + 8, y * 32 + 8, 16, 20);
        return new Rectangle(x * 32 + 5, y * 32 + 14, 22, 16);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // 0-3 direction index (same as vanilla)
        int rotation = level.getObjectRotation(tileX, tileY) % 4;

        // 4 rotations across, tall sprite (32 wide, texture height tall)
        final TextureDrawOptionsEnd options = this.texture.initDraw()
                .sprite(rotation, 0, 32, this.texture.getHeight())
                .light(light)
                .addObjectDamageOverlay(this, level, tileX, tileY)
                .pos(drawX, drawY - this.texture.getHeight() + 32);

        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation,
                            float alpha, PlayerMob player, GameCamera camera) {

        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        this.texture.initDraw()
                .sprite(rotation % 4, 0, 32, this.texture.getHeight())
                .alpha(alpha)
                .draw(drawX, drawY - this.texture.getHeight() + 32);
    }

    @Override
    public Tech[] getCraftingTechs() {
        // Assign what tech/s the station will use
        return new Tech[] { ExampleModTech.EXAMPLE_TECH };
    }
}