package net.theevilreaper.bounce;

import dev.derklaro.aerogel.Injector;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.modules.bridge.impl.platform.minestom.MinestomBridgeExtension;
import io.github.togar2.pvp.MinestomPvP;
import net.minestom.server.MinecraftServer;

/**
 * Initializes some necessary components and starts the {@link MinecraftServer} which is required for the game to run.
 *
 * @version 1.0.0
 * @since .1.0
 * @author theEvilReaper
 */
public class BounceServer {

    static void main() {
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
