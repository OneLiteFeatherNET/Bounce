package net.theevilreaper.bounce;

import io.github.togar2.pvp.MinestomPvP;
import net.minestom.server.MinecraftServer;

public class BounceServer {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinestomPvP.init();
        Bounce bounce = new Bounce();
        bounce.load();
        minecraftServer.start("localhost", 25565);
    }
}
