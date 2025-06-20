package net.theevilreaper.bounce.timer;

import net.theevilreaper.bounce.Bounce;
import de.icevizion.game.api.GameState;
import de.icevizion.game.api.countdown.BukkitCountdown;
import net.titan.spigot.Cloud;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RestartTimer extends BukkitCountdown {

    private final Bounce game;

    public RestartTimer(Bounce game) {
        super( 8);
        this.game = game;
    }

    @Override
    public void start() {
        this.start(game, 0, 20);
    }

    @Override
    public void onStart() {
        game.setGameState(GameState.RESTART);
        Bukkit.broadcastMessage(game.getPrefix() + "§cDie aktuelle Runde ist vorbei!");
        game.getGameUtil().broadcastWinner(game.getGameUtil().getWinner());
    }

    @Override
    public void onEnd() {
        Bukkit.spigot().restart();
    }

    @Override
    public void onTick() {
        switch (getCurrentTime()) {
            case 5:
                Bukkit.broadcastMessage(game.getPrefix() + "§cDieser Server startet sofort neu");
                break;
            case 2:
                for (Player player : Bukkit.getOnlinePlayers()) {
                   // Cloud.getInstance().getPlayer(player).dispatchCommand("/l", new String[0]);
                }
                break;
            default:
                break;
        }
    }
}