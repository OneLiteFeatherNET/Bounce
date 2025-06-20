package net.theevilreaper.bounce.timer;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;
import de.icevizion.game.api.GameState;
import de.icevizion.game.api.countdown.BukkitCountdown;
import org.bukkit.entity.Player;

import java.util.Map;

public class TeleportTask extends BukkitCountdown {

    private final Bounce game;

    public TeleportTask(Bounce game) {
        super(3);
        this.game = game;
    }

    @Override
    public void onStart() {
        game.setGameState(GameState.WARMUP);
    }

    @Override
    public void onEnd() {
        for (Map.Entry<Player, BounceProfile> profiles : game.getProfileService().getProfiles().entrySet()) {
            profiles.getValue().registerJumpRunnable(game, game.getMapManager().getMap());
            profiles.getKey().teleport(game.getMapManager().getMap().getGameSpawn());
            game.getItemUtil().setItem(profiles.getKey());
        }
        game.getGameTimer().start();
    }

    @Override
    public void onTick() {
    }

    @Override
    public void start() {
        this.start(game, 0, 20);
    }
}