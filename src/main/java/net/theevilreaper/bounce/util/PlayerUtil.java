package net.theevilreaper.bounce.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.player.BouncePlayer;

public final class PlayerUtil {

    public void preparePlayers() {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.setLevel(0);
            if (onlinePlayer instanceof BouncePlayer bouncePlayer) {
                bouncePlayer.startRound();
            }
        }
    }
}
