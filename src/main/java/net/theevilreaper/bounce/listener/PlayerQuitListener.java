package net.theevilreaper.bounce.listener;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerQuitListener implements Listener {

    private final Bounce game;

    public PlayerQuitListener(Bounce game) {
        this.game = game;
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        switch (game.getGameState()) {
            case INGAME:
                Optional<BounceProfile> profile = game.getProfileService().getProfile(player);
                if (profile.isPresent()) {
                    event.setQuitMessage("§c● §r" + player.getDisplayName());
                    profile.get().getJumpRunnable().cancel();
                    game.getScoreboardUtil().getScoreboard().resetScores(player.getDisplayName());
                    game.getProfileService().removeProfile(player);
                    game.getGameUtil().checkPlayer();
                } else {
                    event.setQuitMessage(null);
                }
                break;
            default:
                event.setQuitMessage("§c● §r" + player.getDisplayName());
                break;
        }
    }
}