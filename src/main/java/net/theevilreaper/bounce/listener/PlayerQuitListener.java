package net.theevilreaper.bounce.listener;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.bounce.util.GameMessages;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayerQuitListener implements Consumer<PlayerDisconnectEvent> {

    private final Supplier<Phase> phaseSupplier;
    private final PlayerConsumer playerLeave;

    public PlayerQuitListener(@NotNull Supplier<Phase> phaseSupplier, @NotNull PlayerConsumer playerLeave) {
        this.phaseSupplier = phaseSupplier;
        this.playerLeave = playerLeave;
    }

    @Override
    public void accept(@NotNull PlayerDisconnectEvent event) {
        Phase phase = phaseSupplier.get();
        Player player = event.getPlayer();

        switch (phase) {
            case LobbyPhase lobbyPhase -> handleLobbyQuit(lobbyPhase, player);
            case PlayingPhase playingPhase -> handleGameQuit(playingPhase, player);
            default -> {
                Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
            }
        }
    }

    /**
     * Handles the quit logic for the {@link LobbyPhase}.
     *
     * @param lobbyPhase the reference from the phase
     * @param player     the player which is involved
     */
    private void handleLobbyQuit(@NotNull LobbyPhase lobbyPhase, @NotNull Player player) {
        lobbyPhase.checkStopCondition();
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
    }

    /**
     * Handles the quit logic for the {@link PlayingPhase}.
     * @param playingPhase the reference from the phase
     * @param player the player which is involved
     */
    private void handleGameQuit(@NotNull PlayingPhase playingPhase, @NotNull Player player) {
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
        this.playerLeave.accept(player);
        playingPhase.handlePlayerCheck();
    }
}