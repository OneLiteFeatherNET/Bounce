package net.theevilreaper.bounce;

import net.minestom.server.MinecraftServer;
import net.theevilreaper.bounce.setup.BounceSetup;

public class BounceSetupServer {

    static void main() {
        MinecraftServer minecraftServer = MinecraftServer.init();
        BounceSetup bounceSetup = new BounceSetup();
        bounceSetup.initialize();

        minecraftServer.start("localhost", 25565);
    }
}
