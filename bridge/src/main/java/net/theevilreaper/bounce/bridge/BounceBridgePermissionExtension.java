package net.theevilreaper.bounce.bridge;

import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.impl.platform.minestom.MinestomPermissionChecker;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.util.TriState;
import net.minestom.server.extensions.Extension;
import net.onelitefeather.minestom.extensions.processor.ExtensionInfo;

/**
 * Minestom extension that teaches the CloudNet bridge how Bounce resolves permissions.
 * <p>
 * The bridge ships a default checker that only inspects {@code player.getPermissionLevel()}, which
 * is always {@code 0} on a LuckPerms-managed server — maintenance bypass and task-level
 * {@code requiredPermission} checks would therefore reject every player, staff included. This
 * extension registers a checker that reads Adventure's {@link PermissionChecker#POINTER} instead,
 * the same pointer LuckPerms and the {@code /stop} command read, and marks it the registry default.
 * <p>
 * {@link MinestomPermissionChecker} only exists inside the CloudNet bridge's extension classloader,
 * so this glue cannot live in the application. The {@code CloudNet_Bridge} dependency declared below
 * makes this extension load after the bridge and share its classloader hierarchy. Minestom and
 * Adventure come from the application classloader above, so the pointer read here is the very one
 * the player carries.
 */
@ExtensionInfo(
        name = "BounceCloudNetPermissions",
        authors = "theEvilReaper",
        dependencies = "CloudNet_Bridge"
)
public final class BounceBridgePermissionExtension extends Extension {

    @Override
    public void initialize() {
        MinestomPermissionChecker checker = (player, permission) ->
                player.getOrDefault(PermissionChecker.POINTER, PermissionChecker.always(TriState.FALSE))
                        .test(permission);
        ServiceRegistry.registry()
                .registerProvider(MinestomPermissionChecker.class, "bounce-luckperms", checker)
                .markAsDefaultService();
    }

    @Override
    public void terminate() {
    }
}
