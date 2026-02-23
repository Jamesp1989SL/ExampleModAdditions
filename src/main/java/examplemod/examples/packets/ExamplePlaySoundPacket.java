package examplemod.examples.packets;

import examplemod.ExampleMod;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.sound.SoundSettings;

/**
 *  SERVER -> CLIENT packet:
 *  Tells the receiving client to play the example sound at a specific world position (x, y).
 */

public class ExamplePlaySoundPacket extends Packet {

    public final float levelX;
    public final float levelY;

    // MUST HAVE - Used for construction in registry
    public ExamplePlaySoundPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        // Important that it's same order as written in
        levelX = reader.getNextFloat();
        levelY = reader.getNextFloat();
    }

    // Used to construct the packet on the server to then send to desired clients
    public ExamplePlaySoundPacket(float levelX, float levelY) {
        this.levelX = levelX;
        this.levelY = levelY;

        PacketWriter writer = new PacketWriter(this);
        // Important that it's same order as read in
        writer.putNextFloat(levelX);
        writer.putNextFloat(levelY);
    }

    // Runs ONLY on client
    @Override
    public void processClient(NetworkPacket packet, Client client) {
        // When passing a sound to somewhere for playing, you can use the SoundSettings class to
        // specify stuff like pitch, falloff distance, volume, etc.
        SoundSettings soundSettings = new SoundSettings(ExampleMod.EXAMPLE_SOUND)
                .volume(0.8f)
                .basePitch(1.0f)
                .pitchVariance(0.08f)
                .fallOffDistance(900);

        // Finally, play the sound at the position received from the packet
        soundSettings.play(levelX, levelY);
    }

}
