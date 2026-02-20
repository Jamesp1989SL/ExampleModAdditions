package examplemod.examples.objectentity;

import java.awt.Point;

import necesse.entity.mobs.GameDamage;
import necesse.entity.objectEntity.TrapObjectEntity;
import necesse.entity.projectile.TrapArrowProjectile;
import necesse.level.maps.Level;

/*
 * Arrow trap logic.
 * When this trap is triggered by a wire, it shoots an arrow in the direction it faces.
 */
public class ExampleTrapObjectEntity extends TrapObjectEntity {

    // The damage the arrow will deal when it hits something.
    public static final GameDamage DAMAGE = new GameDamage(40.0F, 100.0F, 0.0F, 2.0F, 1.0F);

    public ExampleTrapObjectEntity(Level level, int x, int y) {
        // Cooldown in milliseconds (1000ms = 1 second).
        super(level, x, y, 1000L);

        // This object entity is meant to be recreated, not saved.
        this.shouldSave = false;
    }

    @Override
    public void triggerTrap(int wireID, int dir) {
        // Only the server should spawn projectiles.
        // Also, don't fire again while we're still on cooldown.
        if (isClient() || onCooldown()) return;

        // If a different wire is active at the same time, ignore this trigger.
        if (otherWireActive(wireID)) return;

        // Find the tile position the trap should fire from.
        Point tilePos = getPos(this.tileX, this.tileY, dir);

        // Turn the direction number (0..3) into a simple (x,y) direction.
        Point d = getDir(dir);

        // Convert tile coordinates into pixel coordinates (32 pixels per tile).
        int xPos = tilePos.x * 32;
        int yPos = tilePos.y * 32;

        // Shift the spawn position a bit so the arrow looks like it comes from the correct side.
        if (d.x == 0) xPos += 16;        // shooting up/down: centre of the tile
        else if (d.x == -1) xPos += 30;  // shooting left: near the left edge
        else if (d.x == 1) xPos += 2;    // shooting right: near the right edge

        if (d.y == 0) yPos += 16;        // shooting left/right: centre of the tile
        else if (d.y == -1) yPos += 30;  // shooting up: near the top edge
        else if (d.y == 1) yPos += 2;    // shooting down: near the bottom edge

        // Create and spawn the projectile.
        // The "target" is just one step in the direction we're firing.
        getLevel().entityManager.projectiles.add(new TrapArrowProjectile(
                xPos, yPos,
                xPos + d.x,
                yPos + d.y,
                DAMAGE,
                null
        ));

        // Start the cooldown so it can't fire again instantly.
        startCooldown();
    }

    // Converts 0..3 into up/right/down/left.
    private Point getDir(int dir) {
        if (dir == 0) return new Point(0, -1);  // up
        if (dir == 1) return new Point(1, 0);   // right
        if (dir == 2) return new Point(0, 1);   // down
        if (dir == 3) return new Point(-1, 0);  // left
        return new Point(0, 0);
    }
}