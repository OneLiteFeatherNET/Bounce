package net.theevilreaper.bounce;

import net.hollowcube.minestom.extensions.ExtensionBootstrap;
import net.theevilreaper.bounce.common.bootstrap.ServiceBootstrap;
import net.theevilreaper.bounce.common.permission.LuckPermsSupport;
import net.theevilreaper.bounce.setup.BounceSetup;

public class BounceSetupServer {

    static void main() {
        // minestom-extensions loads platform extensions - the CloudNet bridge and our
        // :bridge permission extension among them - from the extensions/ folder. Running
        // standalone simply loads none. This also performs MinecraftServer.init().
        ExtensionBootstrap bootstrap = ExtensionBootstrap.init();
        LuckPermsSupport.bootstrap();
        BounceSetup bounceSetup = new BounceSetup();
        bounceSetup.initialize();
        ServiceBootstrap.installShutdownHandling();
        bootstrap.start(ServiceBootstrap.resolveBindHost(), ServiceBootstrap.resolveBindPort());
    }
}
