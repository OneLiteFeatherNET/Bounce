package net.theevilreaper.bounce.listener;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.bounce.timer.TeleportPhase;
import net.theevilreaper.bounce.util.GameMessages;
import net.theevilreaper.xerus.api.phase.Phase;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayerQuitListener implements Consumer<PlayerDisconnectEvent> {

    private final Supplier<Phase> phaseSupplier;
    private final PlayerConsumer playerLeave;

    public PlayerQuitListener(Supplier<Phase> phaseSupplier, PlayerConsumer playerLeave) {
        this.phaseSupplier = phaseSupplier;
        this.playerLeave = playerLeave;
    }

    @Override
    public void accept(PlayerDisconnectEvent event) {
        Phase phase = phaseSupplier.get();
        Player player = event.getPlayer();

        switch (phase) {
            case LobbyPhase lobbyPhase -> handleLobbyQuit(lobbyPhase, player);
            case TeleportPhase teleportPhase -> handleTeleportQuit(teleportPhase, player);
            case PlayingPhase playingPhase -> handleGameQuit(playingPhase, player);
            default -> this.handleDefaultLeave(player);
        }
    }

    /**
     * Handles the quit logic for the {@link LobbyPhase}.
     *
     * @param lobbyPhase the reference from the phase
     * @param player     the player which is involved
     */
    private void handleLobbyQuit(LobbyPhase lobbyPhase, Player player) {
        lobbyPhase.checkStopCondition();
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
    }

    /**
     * Handles the quit logic for the {@link TeleportPhase}.
     * If not enough players remain for a round, the teleport gets skipped and the round ends right after it
     * started, the same way as a quit during the {@link PlayingPhase}.
     *
     * @param teleportPhase the reference from the phase
     * @param player        the player which is involved
     */
    private void handleTeleportQuit(TeleportPhase teleportPhase, Player player) {
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
        this.playerLeave.accept(player);
        if (getConnectionManager().getOnlinePlayers().size() > 1) return;

        teleportPhase.finish();
        if (phaseSupplier.get() instanceof PlayingPhase playingPhase) {
            playingPhase.handlePlayerCheck();
        }
    }

    /**
     * Handles the quit logic for the {@link PlayingPhase}.
     *
     * @param playingPhase the reference from the phase
     * @param player       the player which is involved
     */
    private void handleGameQuit(PlayingPhase playingPhase, Player player) {
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
        this.playerLeave.accept(player);
        playingPhase.handlePlayerCheck();
    }

    /**
     * Handles the default quit logic.
     *
     * @param player the player which is involved
     */
    private void handleDefaultLeave(Player player) {
        Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(GameMessages.getLeaveMessage(player));
    }
}