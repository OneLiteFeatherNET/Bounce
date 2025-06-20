package net.theevilreaper.bounce.timer;

import net.theevilreaper.bounce.Bounce;
import de.icevizion.api.Messages;
import de.icevizion.api.title.TitleManager;
import de.icevizion.game.api.GameState;
import de.icevizion.game.api.countdown.BukkitCountdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyTimer extends BukkitCountdown {

    private final Bounce game;

    public LobbyTimer(Bounce game) {
        super(30);
        this.game = game;
    }

    @Override
    public void onStart() {
        game.setGameState(GameState.LOBBY);
    }

    @Override
    public void onEnd() {
        Bukkit.broadcastMessage(game.getPrefix() + "§eAlle Spieler werden in die Arena teleportiert");
        game.getGameUtil().preparePlayers();
        new TeleportTask(game).start();
    }
    
    @Override
    public void onTick() {
        if (game.getMapManager().getMap().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TitleManager.sendActionBar(player, Messages.NOT_ENOUGH_PLAYERS.toString());
            }
            setTime(getStartTime());
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(getCurrentTime());
        }

        switch (getCurrentTime()) {
            case 10:
            case 3:
            case 2:
                Bukkit.broadcastMessage(game.getPrefix() + "§7Das Spiel startet in §3" + getCurrentTime() + " §7Sekunden");
                break;
            case 1:
                Bukkit.broadcastMessage(game.getPrefix() + "§7Das Spiel startet in §3" + getCurrentTime() + " §7Sekunde");
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        this.start(game, 0, 20);
    }
}