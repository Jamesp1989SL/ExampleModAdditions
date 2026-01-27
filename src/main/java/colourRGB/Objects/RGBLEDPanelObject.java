package colourRGB.Objects;

import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.registries.ContainerRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.Color;
import java.util.List;

public class RGBLEDPanelObject extends GameObject {
    public static int RGB_LED_PANEL_CONTAINER; // set in init when you register container
    public GameTexture texture;

    public RGBLEDPanelObject() {
        setItemCategory("wiring");
        setCraftingCategory("wiring");

        this.mapColor = new Color(200, 200, 200);
        this.displayMapTooltip = true;

        this.objectHealth = 1;
        this.toolType = ToolType.ALL;
        this.showsWire = true;

        this.isLightTransparent = true;
        this.roomProperties.add("lights");
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        // 64x64 expected, 2x2 sprites (off/on in X, top/bottom in Y)
        this.texture = GameTexture.fromFile("objects/colourledpanel");
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new RGBLEDPanelObjectEntity(level, x, y);
    }

    @Override
    public Color getMapColor(Level level, int tileX, int tileY) {
        RGBLEDPanelObjectEntity oe = getOE(level, tileX, tileY);
        if (oe == null) return this.mapColor;

        // Option A: always show the chosen colour on the map
        return oe.getColor();

        // Option B: only show chosen colour when powered
        // return isLit(level, tileX, tileY) ? oe.getColor() : this.mapColor;
    }
    private boolean isLit(Level level, int x, int y) {
        return level.wireManager.isWireActiveAny(x, y);
    }

    private RGBLEDPanelObjectEntity getOE(Level level, int x, int y) {
        return getCurrentObjectEntity(level, x, y, RGBLEDPanelObjectEntity.class);
    }

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return true;
    }

    @Override
    public void interact(Level level, int x, int y, PlayerMob player) {
        if (!level.isServer()) return;

        RGBLEDPanelObjectEntity oe = getOE(level, x, y);
        if (oe == null) return;

        PacketOpenContainer pkt = PacketOpenContainer.ObjectEntity(RGB_LED_PANEL_CONTAINER, oe);
        ContainerRegistry.openAndSendContainer(player.getServerClient(), pkt);
    }

    @Override
    public void onWireUpdate(Level level, int layerID, int tileX, int tileY, int wireID, boolean active) {
        level.lightManager.updateStaticLight(tileX, tileY);
    }

    @Override
    public GameLight getLight(Level level, int layerID, int tileX, int tileY) {
        if (!isLit(level, tileX, tileY)) {
            return level.lightManager.newLight(0.0F);
        }

        RGBLEDPanelObjectEntity oe = getOE(level, tileX, tileY);
        Color c = (oe != null) ? oe.getColor() : Color.WHITE;

        // Coloured light. The last number is brightness-ish (tweak to taste).
        return level.lightManager.newLight(c, 1.0F, 75.0F);
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             necesse.engine.gameLoop.tickManager.TickManager tickManager,
                             GameCamera camera, PlayerMob perspective) {

        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        boolean lit = isLit(level, tileX, tileY);
        int spriteX = lit ? 1 : 0;

        RGBLEDPanelObjectEntity oe = getOE(level, tileX, tileY);
        Color c = (oe != null) ? oe.getColor() : Color.WHITE;

        // Vanilla LEDPanel forces bright light on the sprite when lit
        GameLight drawLight = lit ? new GameLight(150.0F) : level.getLightLevel(tileX, tileY);

        final TextureDrawOptionsEnd top = this.texture.initDraw()
                .sprite(spriteX, 0, 32) // top row
                .addObjectDamageOverlay(this, level, tileX, tileY)
                .light(drawLight)
                .pos(drawX, drawY - 32);

        final TextureDrawOptionsEnd bottom = this.texture.initDraw()
                .sprite(spriteX, 1, 32) // bottom row
                .addObjectDamageOverlay(this, level, tileX, tileY)
                .light(drawLight)
                .pos(drawX, drawY);

        if (lit) {
            top.color(c);
            bottom.color(c);
        }

        tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16;
            }

            @Override
            public void draw(necesse.engine.gameLoop.tickManager.TickManager tm) {
                top.draw();
                bottom.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha,
                            PlayerMob player, GameCamera camera) {

        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Preview doesn’t have OE yet; show “off” by default
        int spriteX = 0;

        TextureDrawOptionsEnd top = this.texture.initDraw()
                .sprite(spriteX, 0, 32)
                .alpha(alpha)
                .pos(drawX, drawY - 32);

        TextureDrawOptionsEnd bottom = this.texture.initDraw()
                .sprite(spriteX, 1, 32)
                .alpha(alpha)
                .pos(drawX, drawY);

        top.draw();
        bottom.draw();
    }

    @Override
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips t = super.getItemTooltips(item, perspective);
        t.add("Right-click to choose color");
        return t;
    }
}