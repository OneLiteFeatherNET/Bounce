package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.util.PlayerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GameFinishListener implements Consumer<BounceGameFinishEvent> {

    private final PlayerUtil playerUtil;

    public GameFinishListener(@NotNull PlayerUtil playerUtil) {
        this.playerUtil = playerUtil;
    }

    @Override
    public void accept(@NotNull BounceGameFinishEvent event) {
        if (event.getReason() == BounceGameFinishEvent.Reason.PLAYER_LEFT) return;
        playerUtil.broadcastWinner();
    }
}
