package net.theevilreaper.bounce;

import io.github.togar2.pvp.MinestomPvP;
import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.theevilreaper.bounce.common.bootstrap.ServiceBootstrap;
import net.theevilreaper.bounce.common.permission.LuckPermsSupport;

/**
 * Initializes some necessary components and starts the {@link net.minestom.server.MinecraftServer}
 * which is required for the game to run.
 */
public class BounceServer {

    static void main() {
        // minestom-extensions loads platform extensions - the CloudNet bridge and our
        // :bridge permission extension among them - from the extensions/ folder. Running
        // standalone simply loads none. This also performs MinecraftServer.init().
        ExtensionBootstrap bootstrap = ExtensionBootstrap.init();
        LuckPermsSupport.bootstrap();
        MinestomPvP.init();
        Bounce bounce = new Bounce();
        bounce.load();
        ServiceBootstrap.installShutdownHandling();
        bootstrap.start(ServiceBootstrap.resolveBindHost(), ServiceBootstrap.resolveBindPort());
    }
}
