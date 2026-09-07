package net.theevilreaper.bounce.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.util.GameMessages;

public class StatsCommand extends Command {

    private final ArgumentString playerArgument;

    public StatsCommand() {
        super("stats");
        this.playerArgument = ArgumentType.String("player");
        addSyntax(this::handleOwnStats);
    }

    private void handleOtherStats(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        String playerName = context.get(playerArgument);

        if (playerName.isEmpty()) {
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

        if (!(targetPlayer instanceof BouncePlayer)) {
            sender.sendMessage(GameMessages.NO_PROFILE);
        }
    }

    private void handleOwnStats(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;

        if (!(player instanceof BouncePlayer bouncePlayer)) {
            sender.sendMessage(GameMessages.NO_PROFILE);
            return;
        }

        bouncePlayer.sendStats(false);
    }
}
