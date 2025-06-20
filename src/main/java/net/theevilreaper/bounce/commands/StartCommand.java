package net.theevilreaper.bounce.commands;

import net.theevilreaper.bounce.Bounce;
import net.titan.spigot.Cloud;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Bounce game;

    public StartCommand(Bounce game) {
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player player = (Player)sender;

            if (!Cloud.getInstance().getPlayer(player).hasPermission("game.bounce.start")) return false;

            if (game.getLobbyTimer().isRunning()) {
                game.getLobbyTimer().setTime(10);
                player.sendMessage(game.getPrefix() + "§cDas Spiel startet sofort");
                return true;
            }
        }
        return false;
    }
}
