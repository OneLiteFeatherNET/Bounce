package net.theevilreaper.bounce.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.util.GameMessages;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minestom.server.MinecraftServer.getConnectionManager;

public class PlayerJoinListener implements Consumer<PlayerSpawnEvent> {

    private final Supplier<Phase> phaseSupplier;
    private final PlayerConsumer spawnConsumer;

    public PlayerJoinListener(
            @NotNull Supplier<Phase> phaseSupplier,
            @NotNull PlayerConsumer spawnConsumer
    ) {
        this.phaseSupplier = phaseSupplier;
        this.spawnConsumer = spawnConsumer;
    }

    @Override
    public void accept(@NotNull PlayerSpawnEvent event) {
        var player = event.getPlayer();
        player.setDisplayName(Component.text(player.getUsername()));

        Phase phase = phaseSupplier.get();

        if (event.isFirstSpawn() && phase instanceof LobbyPhase lobbyPhase) {
            Component joinMessage = GameMessages.getJoinMessage(player);
            Audience.audience(getConnectionManager().getOnlinePlayers()).sendMessage(joinMessage);
            lobbyPhase.updatePlayerValues(player);
            lobbyPhase.checkStartCondition();
            this.spawnConsumer.accept(player);
        }
    }
}