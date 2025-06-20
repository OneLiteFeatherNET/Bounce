package net.theevilreaper.bounce.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {

    private static final Component KICK_COMPONENT = Component.text("The game has already started! Unable to join!", NamedTextColor.RED);
    private static final Component FULL_SERVER = Component.text("Unable to join because the server is full!", NamedTextColor.RED);
    private final int maxPlayers;
    private final Supplier<@Nullable Phase> phaseSupplier;
    private final Supplier<@NotNull Instance> instanceSupplier;

    public PlayerConfigurationListener(
            @NotNull Supplier<Phase> phaseSupplier,
            @NotNull Supplier<Instance> instanceSupplier,
            int maxPlayers
    ) {
        this.phaseSupplier = phaseSupplier;
        this.instanceSupplier = instanceSupplier;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void accept(@NotNull AsyncPlayerConfigurationEvent event) {
        Player player = event.getPlayer();
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= maxPlayers) {
            player.kick(FULL_SERVER);
            return;
        }

        Phase phase = phaseSupplier.get();
        if (phase == null) return;

        if (!(phase instanceof LobbyPhase)) {
            player.kick(KICK_COMPONENT);
            return;
        }

        event.setSpawningInstance(instanceSupplier.get());
    }
}