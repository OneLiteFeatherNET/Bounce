package net.theevilreaper.bounce;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.modules.bridge.impl.platform.minestom.MinestomBridgeExtension;
import io.github.togar2.pvp.MinestomPvP;
import net.minestom.server.MinecraftServer;

public class BounceServer {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinestomPvP.init();
        Bounce bounce = new Bounce();
        bounce.load();
        try (InjectionLayer<Injector> layer = InjectionLayer.ext()) {
            layer.instance(MinestomBridgeExtension.class).onLoad();
        }
        minecraftServer.start("localhost", 25565);
    }
}
