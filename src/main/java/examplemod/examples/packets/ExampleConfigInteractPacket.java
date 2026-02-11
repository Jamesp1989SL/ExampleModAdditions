package examplemod.examples.packets;

import examplemod.ExampleMod;
import necesse.engine.Settings;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

public class ExampleConfigInteractPacket extends Packet {

    public final int tileX;
    public final int tileY;

    // requred
    public ExampleConfigInteractPacket(byte[] data) {
        super(data);
        PacketReader r = new PacketReader(this);
        this.tileX = r.getNextInt();
        this.tileY = r.getNextInt();
    }

    // Convenience constructor when sending
    public ExampleConfigInteractPacket(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;

        PacketWriter w = new PacketWriter(this);
        w.putNextInt(tileX);
        w.putNextInt(tileY);
    }

    @Override
    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        if (ExampleMod.settings == null) {
            client.sendChatMessage("[ExampleMod] Settings missing on server.");
            return;
        }

        // 1) Increment server  value
        ExampleMod.settings.exampleInt += 1;

        // 2) Save server config back to disk (server.cfg + cfg/mods/<modid>.cfg)
        Settings.saveServerSettings();

        // 3) Display AFTER increment+save
        boolean enabled = ExampleMod.settings.exampleBoolean;
        int amount = ExampleMod.settings.exampleInt;
        String msg = ExampleMod.settings.exampleString;

        client.sendChatMessage("[ExampleMod] Server config updated (saved):");
        client.sendChatMessage("exampleBoolean: " + enabled);
        client.sendChatMessage("exampleInt: " + amount);
        client.sendChatMessage("exampleString: " + msg);
    }

    @Override
    public void processClient(NetworkPacket packet, Client client) {
        // Nothing needed. Server sends chat messages to the client.
    }
}
