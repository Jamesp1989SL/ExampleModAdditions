package tombstoneondeath;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ObjectRegistry;
import tombstoneondeath.objects.TombstoneObject;

@ModEntry
public class TombstoneOnDeathMod {
    // Keep the object id somewhere easy to access from patches
    public static int TOMBSTONE_OBJECT_ID;

    public void init() {
        // Not obtainable/craftable by default (only spawned by death logic)
        TOMBSTONE_OBJECT_ID = ObjectRegistry.registerObject(
                "tombstoneondeath_tombstone",
                new TombstoneObject(),
                0.0F,
                false,
                false,
                false
        );
    }
}
