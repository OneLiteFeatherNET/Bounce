package net.theevilreaper.bounce.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.profile.ProfileService;

public final class PlayerUtil {

    private final ProfileService profileService;
    private final PushData pushData;

    public PlayerUtil(ProfileService profileService, PushData pushData) {
        this.profileService = profileService;
        this.pushData = pushData;
    }

    public void preparePlayers() {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.setLevel(0);
            BounceProfile profile = this.profileService.add(onlinePlayer);
            profile.registerJumpRunnable(pushData);
        }
    }
}
