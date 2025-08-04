package net.theevilreaper.bounce.setup.listener;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.onelitefeather.guira.data.SetupData;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.bounce.common.util.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public final class PlayerDisconnectListener implements Consumer<PlayerDisconnectEvent> {

    private final OptionalSetupDataGetter profileFunction;

    public PlayerDisconnectListener(@NotNull OptionalSetupDataGetter profileFunction) {
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

        Optional<@Nullable SetupData> setupData = this.profileFunction.get(player.getUuid());
        setupData.ifPresent(SetupData::reset);
    }
}
