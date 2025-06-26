package net.theevilreaper.bounce.setup.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.theevilreaper.bounce.common.util.Messages;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public final class PlayerDisconnectListener implements Consumer<PlayerDisconnectEvent> {

    private final Function<UUID, Optional<BounceData>> profileFunction;

    public PlayerDisconnectListener(@NotNull Function<UUID, Optional<BounceData>> profileFunction) {
        this.profileFunction = profileFunction;
    }

    @Override
    public void accept(@NotNull PlayerDisconnectEvent event) {
        Player player = event.getPlayer();

        Component joinMessage = Messages.withPrefix(Component.text(player.getUsername(), NamedTextColor.AQUA))
                .append(Component.space())
                .append(Component.text("left the server", NamedTextColor.GRAY));
        Audience.audience(MinecraftServer.getConnectionManager().getOnlinePlayers())
                .sendMessage(joinMessage);

        Optional<BounceData> dataOptional = this.profileFunction.apply(player.getUuid());

        if (dataOptional.isEmpty()) return;

        BounceData bounceData = dataOptional.get();
        bounceData.reset();
    }
}
