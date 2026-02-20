package examplemod.examples.ai;

import examplemod.examples.packets.ExamplePlaySoundPacket;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;

/**
 * Runs exactly once, server-side, and tells nearby clients to play a sound
 * at the mob's current position via ExamplePlaySoundPacket.
 * Returns FAILURE so the parent tree continues normally (this leaf never "takes over").
 */
public class ExampleAILeaf<T extends Mob> extends AINode<T> {

    // Ensure this only fires once per mob instance.
    private boolean didRun = false;

    @Override
    protected void onRootSet(AINode<T> root, T mob, Blackboard<T> blackboard) {
        // No setup needed.
    }

    @Override
    public void init(T mob, Blackboard<T> blackboard) {
        // No init needed.
    }

    @Override
    public AINodeResult tick(T mob, Blackboard<T> blackboard) {
        // Run once.
        if (didRun) return AINodeResult.FAILURE;
        didRun = true;

        // Only the server should broadcast packets to clients.
        if (mob == null || !mob.isServer()) return AINodeResult.FAILURE;

        if (mob.getLevel() != null && mob.getLevel().getServer() != null) {
            mob.getLevel().getServer().network.sendToClientsWithEntity(
                    new ExamplePlaySoundPacket(mob.x, mob.y),
                    mob
            );
        }

        return AINodeResult.FAILURE;
    }
}