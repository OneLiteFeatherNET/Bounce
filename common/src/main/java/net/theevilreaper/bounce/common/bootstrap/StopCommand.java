package net.theevilreaper.bounce.common.bootstrap;

import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.TriState;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.common.permission.LuckPermsSupport;

/**
 * Shuts the service down cleanly. Reserved for non-player senders (the console/CloudNet) and
 * players holding {@value #PERMISSION}, since a service should not be stoppable by regular players.
 * <p>
 * Unlike every other permission check in this codebase, this command does NOT fall back to
 * granting access when LuckPerms is absent from the class path — a joining player being able to
 * stop a live service is a materially larger risk than the general "grant everything locally"
 * fallback other permissions rely on for local/test runs. In that mode, only non-player senders
 * (console, CloudNet's stdin stop signal) can run this command.
 */
public final class StopCommand extends Command {

    private static final String PERMISSION = "bounce.command.stop";

    /**
     * Creates a new instance of the {@link StopCommand} and wires its condition and executor.
     */
    public StopCommand() {
        super("stop");
        setCondition((sender, commandString) -> !(sender instanceof Player) || (LuckPermsSupport.isPresent() && hasStopPermission(sender)));
        setDefaultExecutor((sender, context) -> Thread.ofPlatform().name("bounce-shutdown").start(() -> {
            MinecraftServer.stopCleanly();
            System.exit(0);
        }));
    }

    /**
     * Checks whether the given sender is allowed to run this command.
     * <p>
     * Reads Adventure's {@link PermissionChecker#POINTER}, which the player implementation backs
     * with LuckPerms (see {@code PermissionAwarePlayer}). A sender without that pointer is denied.
     *
     * @param sender the sender to check
     * @return {@code true} if the sender holds {@value #PERMISSION}, {@code false} otherwise
     */
    private static boolean hasStopPermission(CommandSender sender) {
        return sender.getOrDefault(PermissionChecker.POINTER, PermissionChecker.always(TriState.FALSE))
                .test(PERMISSION);
    }
}
