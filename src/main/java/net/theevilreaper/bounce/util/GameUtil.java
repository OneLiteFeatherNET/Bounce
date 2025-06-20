package net.theevilreaper.bounce.util;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;

import de.icevizion.api.item.FireworkFactory;
import de.icevizion.api.title.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameUtil {

    private final Bounce game;

    public GameUtil(Bounce plugin) {
        this.game = plugin;
    }

    public void checkPlayer() {
        switch (Bukkit.getOnlinePlayers().size() - 1) {
            case 1:
                broadcastWinner(getWinner());
                game.getGameTimer().stop();
                break;
            case 0:
                Bukkit.broadcastMessage("§cKeine Spieler mehr da.. Starte Server neu");
                game.getGameTimer().stop();
                break;
            default:
                break;
        }
    }

    public void preparePlayers() {
        game.getScoreboardUtil().initGameScoreboard();
        for (Player player : Bukkit.getOnlinePlayers()) {
            game.getProfileService().addProfile(player, new BounceProfile(player));
            player.setLevel(0);
            game.getScoreboardUtil().getScoreboard().getObjective("InGame").getScore(player.getDisplayName()).setScore(0);
        }
    }

    private void playFirework(Player player) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                switch (i) {
                    case 5:
                        cancel();
                        break;
                    default:
                        new FireworkFactory().addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).
                                withColor(Color.TEAL).build()).setPower(1).spawn(player.getLocation());
                        break;
                }
                i++;
            }
        }.runTaskTimer(game, 10, 10);
    }

    public Player getWinner() {
        int highestValue = 0;
        Player player = null;
        for (BounceProfile profile : game.getProfileService().getProfiles().values()) {
            if (profile.getPoints() >= highestValue) {
                highestValue = profile.getPoints();
                player = profile.getPlayer();
            }
        }
        return player;
    }

    public void broadcastWinner(Player player) {
        playFirework(player);
        for (BounceProfile profiles : game.getProfileService().getProfiles().values()) {
            if (profiles.getPlayer() == player) {
                profiles.sendStats(true);
            } else {
                profiles.sendStats(false);
            }
            TitleManager.sendTitles(profiles.getPlayer(), player.getDisplayName(), "§7hat das Spiel gewonnen", 10, 40, 30);
        }
    }
}