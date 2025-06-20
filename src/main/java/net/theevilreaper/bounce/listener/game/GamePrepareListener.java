package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.GamePrepareEvent;
import net.theevilreaper.bounce.util.PlayerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GamePrepareListener implements Consumer<GamePrepareEvent> {

    private final PlayerUtil playerUtil;

    public GamePrepareListener(@NotNull PlayerUtil playerUtil) {
        this.playerUtil = playerUtil;
    }

    @Override
    public void accept(@NotNull GamePrepareEvent event) {
        playerUtil.preparePlayers();
    }
}
