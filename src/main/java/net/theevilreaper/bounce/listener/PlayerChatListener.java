package net.theevilreaper.bounce.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.theevilreaper.bounce.common.util.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PlayerChatListener implements Consumer<PlayerChatEvent> {

    @Override
    public void accept(@NotNull PlayerChatEvent event) {
        Player player = event.getPlayer();
        Component message = player.getDisplayName().append(Messages.SEPARATOR).append(Component.text(event.getRawMessage(), NamedTextColor.WHITE));
        event.setFormattedMessage(message);
    }
}
