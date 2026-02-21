package examplemod.examples.objects;

import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.container.CraftingStationObject;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.SideMultiTile;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptionsList;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import java.util.List;
import java.awt.Color;
import java.awt.Rectangle;

public class ExampleWorkstationDuo2Object extends CraftingStationObject {
    public GameTexture texture;

    // This stores the OTHER object id (the main/masterpiece)
    protected int counterID;

    public ExampleWorkstationDuo2Object() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(132, 91, 25);
        this.isLightTransparent = true;
        this.hoverHitbox = new Rectangle(0, -16, 32, 48);
    }
    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {

        // Collision is in pixels, not tiles.
        // Each tile is 32x32 pixels.
        //
        // We return a rectangle that blocks movement for this object.
        // Different rotations can have different shapes, so we pick a different
        // rectangle depending on which way the object is facing.

        if (rotation == 0)
            // Rotation 0: inset a bit from the tile edges (4px in from left/top),
            // and make it mostly tall (24x28) so it doesn't fill the whole tile.
            return new Rectangle(x * 32 + 4, y * 32 + 4, 24, 28);

        if (rotation == 1)
            // Rotation 1: shape is wider / shorter.
            // Starts at left edge, 6px down, size 26x20.
            return new Rectangle(x * 32, y * 32 + 6, 26, 20);

        if (rotation == 2)
            // Rotation 2: similar to rotation 0 but shifted upward a bit.
            // Starts 4px in from left, at the top edge, size 24x26.
            return new Rectangle(x * 32 + 4, y * 32, 24, 26);

        // Rotation 3 (default): same idea as rotation 1 but shifted right a bit.
        // Starts 6px in from left and 6px down, size 26x20.
        return new Rectangle(x * 32 + 6, y * 32 + 6, 26, 20);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        // How bright to draw this tile (depends on time of day / light sources)
        GameLight light = level.getLightLevel(tileX, tileY);

        // Convert tile position -> screen position
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Which way the object is facing (0-3)
        int rotation = level.getObjectRotation(tileX, tileY);

        // Store draw calls here, then draw them at the end
        final DrawOptionsList options = new DrawOptionsList();

        // This is the "partner" half of the duo workstation.
        if (rotation == 0) {
            // Rotation 0: draw 2 sprites stacked (top + bottom)
            options.add(this.texture.initDraw().sprite(0, 0, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY) // cracks if damaged
                    .light(light)                                       // apply lighting
                    .pos(drawX, drawY - 32));                            // top piece (1 tile above)

            options.add(this.texture.initDraw().sprite(0, 1, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));                                 // bottom piece (on the tile)

        } else if (rotation == 1) {
            // Rotation 1: draw 2 sprites stacked (top + bottom)
            options.add(this.texture.initDraw().sprite(1, 5, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));                            // top

            options.add(this.texture.initDraw().sprite(1, 6, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));                                 // bottom

            // Optional extra: animated flame overlay.
            // We aren't using this here ? what uses this ?
            // We pick a different flame sprite every 300 ticks:
            // worldTime % 1200 gives a loop, / 300 makes it 0..3 (4 frames).
            int flameSprite = (int) (level.getWorldEntity().getWorldTime() % 1200L / 300L);

            // flameSprite % 2 picks column 0 or 1
            // 7 + flameSprite / 2 picks row 7 or 8
            // (So the 4 frames are arranged in a 2x2 block starting at (0,7))

            options.add(this.texture.initDraw().sprite(flameSprite % 2, 7 + flameSprite / 2, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));                                 // flame sits on the bottom tile

        } else if (rotation == 2) {
            // Rotation 2: only 1 sprite needed for this half
            options.add(this.texture.initDraw().sprite(1, 2, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));

        } else {
            // Rotation 3: draw 2 sprites stacked (top + bottom)
            options.add(this.texture.initDraw().sprite(0, 3, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));                            // top

            options.add(this.texture.initDraw().sprite(0, 4, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));                                 // bottom
        }

        // Tell the engine "here is how to draw this object tile"
        // LevelSortedDrawable makes sure it sorts correctly with other objects/entities.
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16; // normal sorting height (middle of the tile)
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw(); // run all the draw calls we added above
            }
        });
    }

    @Override
    public void loadTextures() {
        // Usually both pieces share the same texture sheet
        this.texture = GameTexture.fromFile("objects/exampleworkstationduo");
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        // Same 1x2 multi-tile, but THIS is NOT the masterpiece (false)
        return new SideMultiTile(0, 0, 1, 2, rotation, false, getID(), this.counterID);
    }
}