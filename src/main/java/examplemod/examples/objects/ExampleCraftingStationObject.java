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
    }

    @Override
    public void loadTextures() {
        super.loadTextures();

        this.texture = GameTexture.fromFile("objects/examplecraftingstation");
    }

    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {

        // This is the "solid" hitbox for the object
        // x,y are tile coords, but Rectangle uses pixels
        // Each tile is 32x32 pixels, so we multiply by 32 to get pixel position.

        if (rotation % 2 == 0)
            // Rotations 0 and 2 (even):
            // A narrower hitbox centered in the tile.
            // Starts 8px in from left/top, 16px wide, 20px tall.
            return new Rectangle(x * 32 + 8, y * 32 + 8, 16, 20);

        // Rotations 1 and 3 (odd):
        // A wider hitbox, shifted down a bit (so the "base" is lower).
        // Starts 5px in from left, 14px down, 22px wide, 16px tall.
        return new Rectangle(x * 32 + 5, y * 32 + 14, 22, 16);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        // Light level at this tile (affects brightness)
        GameLight light = level.getLightLevel(tileX, tileY);

        // Convert tile coords -> screen coords
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Object rotation/direction (0-3)
        int rotation = level.getObjectRotation(tileX, tileY) % 4;

        /*
         * Draw a "tall" object sprite:
         *   4 rotations across the texture (x = 0..3)
         *   each rotation is 32px wide
         *   the height is the full texture height
         *
         * We also shift it upward so the bottom of the sprite sits on the tile.
         * The "+ 32" part is the same anchoring style used by objects like the Alchemy Table.
         */
        final TextureDrawOptionsEnd options = this.texture.initDraw()
                .sprite(rotation, 0, 32, this.texture.getHeight())           // pick rotation column
                .light(light)                                                // apply lighting
                .addObjectDamageOverlay(this, level, tileX, tileY)            // cracks if damaged
                .pos(drawX, drawY - this.texture.getHeight() + 32);           // anchor sprite to tile

        // Add as a sorted drawable so it layers correctly with mobs/objects in the world
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16; // standard sort height for many crafting objects
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw(); // perform the draw call we built above
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation,
                            float alpha, PlayerMob player, GameCamera camera) {

        // Preview is the "ghost" you see before placing the object
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Same sprite logic as addDrawables, but with transparency (alpha)
        this.texture.initDraw()
                .sprite(rotation % 4, 0, 32, this.texture.getHeight())        // choose rotation column
                .alpha(alpha)                                                 // make it see-through
                .draw(drawX, drawY - this.texture.getHeight() + 32);           // same anchor offset
    }

    @Override
    public Tech[] getCraftingTechs() {
        // Assign what tech/s the station will use
        return new Tech[] { ExampleModTech.EXAMPLE_TECH };
    }
}