package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.GamePrepareEvent;
import net.theevilreaper.bounce.util.PlayerUtil;

import java.util.function.Consumer;

public class GamePrepareListener implements Consumer<GamePrepareEvent> {

    private final PlayerUtil playerUtil;

    public GamePrepareListener(PlayerUtil playerUtil) {
        this.playerUtil = playerUtil;
    }

    @Override
    public void accept(GamePrepareEvent event) {
        playerUtil.preparePlayers();
    }
}
