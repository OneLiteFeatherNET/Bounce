package net.theevilreaper.bounce.listener.game;

import net.theevilreaper.bounce.event.GamePrepareEvent;
import net.theevilreaper.bounce.util.PlayerUtil;

import java.util.function.Consumer;

public class GamePrepareListener implements Consumer<GamePrepareEvent> {

    @Override
    public void accept(GamePrepareEvent event) {
        PlayerUtil.preparePlayers();
    }
}
