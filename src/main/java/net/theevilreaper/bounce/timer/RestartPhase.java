package net.theevilreaper.bounce.timer;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.util.GameMessages;
import net.theevilreaper.xerus.api.phase.TimedPhase;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class RestartPhase extends TimedPhase {

    private static final Component KICK_MESSAGE = Component.text("The game is over. Thanks for playing it. <3", NamedTextColor.RED);

    public RestartPhase() {
        super("Restart", ChronoUnit.SECONDS, 1);
        this.setCurrentTicks(15);
        this.setEndTicks(-1);
    }

    @Override
    protected void onFinish() {
        MinecraftServer.stopCleanly();
    }

    @Override
    public void onUpdate() {
        switch (getCurrentTicks()) {
            case 10, 3, 2, 1 -> broadcast(GameMessages.getStopTime(getCurrentTicks()));
            case 0 -> {
                for (Player onlinePlayer : getConnectionManager().getOnlinePlayers()) {
                    onlinePlayer.kick(KICK_MESSAGE);
                }
            }
            default -> {
                // Nothing to do here
            }
        }
    }

    private void broadcast(@NotNull Component component) {
        Audience.audience(getConnectionManager().getOnlinePlayers())
                .sendMessage(component);
    }
}