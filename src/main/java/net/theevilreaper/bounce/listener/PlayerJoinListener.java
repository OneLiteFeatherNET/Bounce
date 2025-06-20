package net.theevilreaper.bounce.listener;

import net.theevilreaper.bounce.Bounce;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Bounce game;

    public PlayerJoinListener(Bounce game) {
        this.game = game;
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.getInventory().clear();
        player.setScoreboard(game.getScoreboardUtil().getScoreboard());

        switch (game.getGameState()) {
            case LOBBY:
                event.setJoinMessage("§a● §r" + event.getPlayer().getDisplayName());
                break;
            default:
                event.setJoinMessage(null);
                break;
        }
    }
}