package net.theevilreaper.bounce.timer;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;
import de.icevizion.game.api.GameState;
import de.icevizion.game.api.countdown.BukkitCountdown;
import org.bukkit.Bukkit;

public class InGameTimer extends BukkitCountdown {

    private final Bounce game;

    public InGameTimer(Bounce plugin) {
        super(180);
        this.game = plugin;
    }

    @Override
    public void onStart() {
        game.setGameState(GameState.INGAME);
    }

    @Override
    public void onEnd() {
        for (BounceProfile bounceProfile : game.getProfileService().getProfiles().values()) {
            bounceProfile.getJumpRunnable().cancel();
        }

        game.getScoreboardUtil().updateGameScoreboardDisplayName(0);
        game.getRestartTimer().start();
    }

    @Override
    public void onTick() {
        game.getScoreboardUtil().updateGameScoreboardDisplayName(getCurrentTime());
        switch (getCurrentTime()) {
            case 10:
            case 3:
            case 2:
            case 1:
                Bukkit.broadcastMessage(game.getPrefix() + "§cEs verbleiben noch §6" + getCurrentTime() + " §cSekunden");
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