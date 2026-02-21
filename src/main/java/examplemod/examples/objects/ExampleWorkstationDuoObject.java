package examplemod.examples.objects;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Rectangle;

import examplemod.Loaders.ExampleModTech;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.registries.ObjectRegistry;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.recipe.Tech;
import necesse.level.gameObject.ObjectPlaceOption;
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

//TODO figure out item art

public class ExampleWorkstationDuoObject extends CraftingStationObject {
    public GameTexture texture;

    // This stores the OTHER object id (the partner piece)
    protected int counterID;

    public ExampleWorkstationDuoObject() {
        super(new Rectangle(32, 32));
        this.mapColor = new Color(132, 91, 25);
        this.isLightTransparent = true;

        // Optional: gives a nicer hover area (same idea as vanilla duo)
        this.hoverHitbox = new Rectangle(0, -16, 32, 48);
    }

    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {

        // This returns the "solid" hitbox for the object (what blocks movement).
        // Coordinates are in pixels:
        // - x,y are tile coords
        // - each tile is 32x32 pixels
        //
        // The object can be rotated (0-3), and the collision box changes slightly
        // depending on which way it's facing.

        if (rotation == 0)
            // Rotation 0: a taller box that starts at the top of the tile.
            // 4px inset from the left, 24px wide, 26px tall.
            return new Rectangle(x * 32 + 4, y * 32, 24, 26);

        if (rotation == 1)
            // Rotation 1: a shorter/wider box, shifted in from left and down.
            // Starts 6px in and 6px down, 26px wide, 20px tall.
            return new Rectangle(x * 32 + 6, y * 32 + 6, 26, 20);

        if (rotation == 2)
            // Rotation 2: the tallest version (almost fills the tile vertically).
            // Starts 4px in and 4px down, 24px wide, 28px tall.
            return new Rectangle(x * 32 + 4, y * 32 + 4, 24, 28);

        // Rotation 3 (default): similar size to rotation 1 but shifted left a bit.
        // Starts at the left edge, 6px down, 26px wide, 20px tall.
        return new Rectangle(x * 32, y * 32 + 6, 26, 20);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {

        // Light affects brightness (day/night, torches, etc.)
        GameLight light = level.getLightLevel(tileX, tileY);

        // Convert tile coordinates to screen draw coordinates
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Rotation is 0-3 This decides which sprites to draw.
        int rotation = level.getObjectRotation(tileX, tileY);

        // Build a list of draw calls to be called at the end
        final DrawOptionsList options = new DrawOptionsList();

        /*
         * This object is part of a 1x2 object
         * Depending on rotation, the visible shape changes:
         *  Some rotations draw only 1 sprite
         *  Some rotations draw 2 sprites stacked vertically (top + bottom)
         *
         * sprite(x, y, 32) means:
         *  take the 32x32 sprite at grid position (x, y) from the texture sheet
         */

        if (rotation == 0) {
            // Rotation 0: this tile only needs 1 sprite
            options.add(this.texture.initDraw().sprite(0, 2, 32)
                    // Adds cracks/visual damage if the object is damaged
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    // Apply lighting
                    .light(light)
                    // Draw at the tile position
                    .pos(drawX, drawY));

        } else if (rotation == 1) {
            // Rotation 1: draw 2 sprites (top part above, bottom part on the tile)
            options.add(this.texture.initDraw().sprite(0, 5, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    // Top sprite sits 1 tile (32px) above
                    .pos(drawX, drawY - 32));

            options.add(this.texture.initDraw().sprite(0, 6, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    // Bottom sprite sits on the tile
                    .pos(drawX, drawY));

        } else if (rotation == 2) {
            // Rotation 2: also 2 sprites (top + bottom), but from different sheet coords
            options.add(this.texture.initDraw().sprite(1, 0, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));

            options.add(this.texture.initDraw().sprite(1, 1, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));

        } else {
            // Rotation 3: also 2 sprites (top + bottom), again different sheet coords
            options.add(this.texture.initDraw().sprite(1, 3, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));

            options.add(this.texture.initDraw().sprite(1, 4, 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));
        }

        /*
         * Necesse draws objects using LevelSortedDrawable so they sort correctly
         * in front or behind other things.
         *
         * We add ONE drawable entry for this tile, and inside it, we draw our options list.
         */
        list.add(new LevelSortedDrawable(this, tileX, tileY) {

            @Override
            public int getSortY() {
                // Sort position within the tile.
                // 16 = middle of tile because 1 tile = 32
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                // Actually draw everything we queued up above
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation,
                            float alpha, PlayerMob player, GameCamera camera) {

        // Preview uses camera coords too
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        /*
         * drawPreview is shown when the player is holding the object before placing it.
         * For a DUO workstation we draw multiple sprites so the player sees
         * the full shape that will be placed (both tiles, not just the clicked one).
         *
         * alpha controls transparency of the preview (ghost placement)
         */

        if (rotation == 0) {
            // Draw this main tile sprite
            this.texture.initDraw().sprite(0, 2, 32).alpha(alpha).draw(drawX, drawY);

            // Draw the partner tile pieces above it (2 tiles tall)
            this.texture.initDraw().sprite(0, 0, 32).alpha(alpha).draw(drawX, drawY - 64);
            this.texture.initDraw().sprite(0, 1, 32).alpha(alpha).draw(drawX, drawY - 32);

        } else if (rotation == 1) {
            // Draw this tile (2-high)
            this.texture.initDraw().sprite(0, 5, 32).alpha(alpha).draw(drawX, drawY - 32);
            this.texture.initDraw().sprite(0, 6, 32).alpha(alpha).draw(drawX, drawY);

            // Draw the partner tile to the RIGHT (x + 32)
            this.texture.initDraw().sprite(1, 5, 32).alpha(alpha).draw(drawX + 32, drawY - 32);
            this.texture.initDraw().sprite(1, 6, 32).alpha(alpha).draw(drawX + 32, drawY);

        } else if (rotation == 2) {
            // Draw this tile (2-high)
            this.texture.initDraw().sprite(1, 0, 32).alpha(alpha).draw(drawX, drawY - 32);
            this.texture.initDraw().sprite(1, 1, 32).alpha(alpha).draw(drawX, drawY);

            // Draw the partner tile below (y + 32)
            this.texture.initDraw().sprite(1, 2, 32).alpha(alpha).draw(drawX, drawY + 32);

        } else {
            // Draw this tile (2-high)
            this.texture.initDraw().sprite(1, 3, 32).alpha(alpha).draw(drawX, drawY - 32);
            this.texture.initDraw().sprite(1, 4, 32).alpha(alpha).draw(drawX, drawY);

            // Draw the partner tile to the LEFT (x - 32)
            this.texture.initDraw().sprite(0, 3, 32).alpha(alpha).draw(drawX - 32, drawY - 32);
            this.texture.initDraw().sprite(0, 4, 32).alpha(alpha).draw(drawX - 32, drawY);
        }
    }

    @Override
    public ArrayList<ObjectPlaceOption> getPlaceOptions(Level level, int levelX, int levelY,
                                                        PlayerMob playerMob, int playerDir,
                                                        boolean offsetMultiTile) {
        // duo workstations shift the direction by -1
        // so the placed "rotation" matches how the sprites are laid out.
        int fixedDir = Math.floorMod(playerDir - 1, 4);
        return super.getPlaceOptions(level, levelX, levelY, playerMob, fixedDir, offsetMultiTile);
    }

    @Override
    public GameMessage getNewLocalization() {
        return new LocalMessage("object", "exampleworkstationduo");
    }

    @Override
    public void loadTextures() {
        // objects/exampleworkstationduo.png
        this.texture = GameTexture.fromFile("objects/exampleworkstationduo");
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        // 1x2 “side” multi-tile, THIS is the masterpiece (true)
        // The ids array order matches the two tiles in the multi-tile.
        return new SideMultiTile(0, 1, 1, 2, rotation, true, this.counterID, getID());
    }

    @Override
    public Tech[] getCraftingTechs() {
        // Use whatever techs you want. This is just an example.
        return new Tech[] { ExampleModTech.EXAMPLE_TECH };
    }

    // Call this from your mod init to register BOTH pieces
    public static int[] register() {
        ExampleWorkstationDuoObject main = new ExampleWorkstationDuoObject();
        ExampleWorkstationDuo2Object part = new ExampleWorkstationDuo2Object();

        int mainID = ObjectRegistry.registerObject("exampleworkstationduo", main, 10.0F, true);
        int partID = ObjectRegistry.registerObject("exampleworkstationduo2", part, 0.0F, false);

        // Link them together (this is the key)
        main.counterID = partID;
        part.counterID = mainID;

        return new int[] { mainID, partID };
    }
}