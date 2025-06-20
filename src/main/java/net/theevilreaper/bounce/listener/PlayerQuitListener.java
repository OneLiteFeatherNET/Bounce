package net.theevilreaper.bounce.listener;

import net.kyori.adventure.audience.Audience;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.util.GameMessages;
import net.theevilreaper.xerus.api.phase.LinearPhaseSeries;
import net.theevilreaper.xerus.api.phase.Phase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayerQuitListener implements Consumer<PlayerDisconnectEvent> {

    private final Supplier<Phase> phaseSupplier;

    public PlayerQuitListener(@NotNull Supplier<Phase> phaseSupplier) {
        this.phaseSupplier = phaseSupplier;
    }

    @Override
    public void accept(@NotNull PlayerDisconnectEvent event) {
        Phase phase = phaseSupplier.get();
        Player player = event.getPlayer();

        if (phase instanceof LobbyPhase lobbyPhase) {
            this.handleLobbyQuit(lobbyPhase,player);
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

    @EventHandler
    public void on(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        switch (game.getGameState()) {
            case INGAME:
                Optional<BounceProfile> profile = game.getProfileService().getProfile(player);
                if (profile.isPresent()) {
                    event.setQuitMessage("§c● §r" + player.getDisplayName());
                    profile.get().getJumpRunnable().cancel();
                    game.getScoreboardUtil().getScoreboard().resetScores(player.getDisplayName());
                    game.getProfileService().removeProfile(player);
                    game.getGameUtil().checkPlayer();
                } else {
                    event.setQuitMessage(null);
                }
                break;
            default:
                event.setQuitMessage("§c● §r" + player.getDisplayName());
                break;
        }
    }
}