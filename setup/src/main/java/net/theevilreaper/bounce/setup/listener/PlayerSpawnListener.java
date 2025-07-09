package net.theevilreaper.bounce.setup.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.util.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class PlayerSpawnListener implements Consumer<PlayerSpawnEvent> {

    private final PlayerConsumer itemConsumer;
    private final PlayerConsumer teleportConsumer;

    public PlayerSpawnListener(@NotNull PlayerConsumer itemConsumer, @NotNull PlayerConsumer teleportConsumer) {
        this.itemConsumer = itemConsumer;
        this.teleportConsumer = teleportConsumer;
    }

    @Override
    public void accept(@NotNull PlayerSpawnEvent event) {
        Player player = event.getPlayer();

        if (event.isFirstSpawn()) {
            Component joinMessage = Messages.withPrefix(Component.text(player.getUsername(), NamedTextColor.AQUA))
                    .append(Component.space())
                    .append(Component.text("joined the server", NamedTextColor.GRAY));
            Audience.audience(MinecraftServer.getConnectionManager().getOnlinePlayers())
                    .sendMessage(joinMessage);
            this.itemConsumer.accept(player);
            this.teleportConsumer.accept(player);
        }
    }
}
