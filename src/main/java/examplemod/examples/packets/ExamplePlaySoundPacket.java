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

    public final float x;
    public final float y;

    // Decode (CLIENT receiving)
    public ExamplePlaySoundPacket(byte[] data) {
        super(data);
        PacketReader r = new PacketReader(this);
        x = r.getNextFloat();
        y = r.getNextFloat();
    }

    // Encode (SERVER sending)
    public ExamplePlaySoundPacket(float x, float y) {
        this.x = x;
        this.y = y;

        PacketWriter w = new PacketWriter(this);
        w.putNextFloat(x);
        w.putNextFloat(y);
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
        soundSettings.play(x, y);
    }

}
