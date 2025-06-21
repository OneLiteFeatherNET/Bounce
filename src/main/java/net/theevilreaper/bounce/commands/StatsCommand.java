package net.theevilreaper.bounce.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentTime;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.util.GameMessages;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class StatsCommand extends Command {

    private final Function<Player, BounceProfile> profileFunction;
    private final ArgumentString playerArgument;
    public StatsCommand(@NotNull Function<Player, BounceProfile> profileFunction) {
        super("stats");
        this.profileFunction = profileFunction;
        this.playerArgument = ArgumentType.String("player");
        addSyntax(this::handleOwnStats);
    }

    private void handleOtherStats(@NotNull CommandSender sender, @NotNull CommandContext context) {
        Player player = (Player) sender;
        String playerName = context.get(playerArgument);

        if (playerName == null || playerName.isEmpty()) {
            sender.sendMessage(GameMessages.INVALID_PLAYER_NAME);
            return;
        }

        Player targetPlayer = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(playerName);

        if (targetPlayer == null) {
            sender.sendMessage(GameMessages.PLAYER_NOT_FOUND);
            return;
        }

        if (targetPlayer.getUuid().equals(player.getUuid())) {
            this.handleOwnStats(sender, context);
            return;
        }

        BounceProfile profile = profileFunction.apply(targetPlayer);

        if (profile == null) {
            sender.sendMessage(GameMessages.NO_PROFILE);
            return;
        }

    }

    private void handleOwnStats(@NotNull CommandSender sender, @NotNull CommandContext context) {
        Player player = (Player) sender;
        BounceProfile profile = profileFunction.apply(player);

        if (profile == null) {
            sender.sendMessage(GameMessages.NO_PROFILE);
            return;
        }

        profile.sendStats(false);
    }
}
