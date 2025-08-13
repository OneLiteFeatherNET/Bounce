package net.theevilreaper.bounce.setup.listener.map;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.Task;
import net.onelitefeather.guira.SetupDataService;
import net.onelitefeather.guira.data.SetupData;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.bounce.common.util.Messages;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.map.MapSetupSelectEvent;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;

import static net.theevilreaper.bounce.setup.util.SetupTags.SETUP_TAG;

public final class MapSetupSelectListener implements Consumer<MapSetupSelectEvent> {

    private final FileHandler fileHandler;
    private final SetupDataService setupDataService;

    public MapSetupSelectListener(
            @NotNull FileHandler fileHandler,
            @NotNull SetupDataService setupDataService
    ) {
        this.fileHandler = fileHandler;
        this.setupDataService = setupDataService;
    }

    @Override
    public void accept(@NotNull MapSetupSelectEvent event) {
        Player player = event.getPlayer();

        Optional<SetupData> setupData = this.setupDataService.get(player.getUuid());

        if (setupData.isPresent()) {
            // If this condition is reached the setup is fucked up
            player.sendMessage("You already have a map selected");
            return;
        }

        MapEntry mapEntry = event.getMapEntry();
        BounceData data = new BounceData(player.getUuid(), mapEntry, this.fileHandler);

        Component message = Messages.withPrefix(Component.text("You selected the map: ", NamedTextColor.GRAY))
                .append(Component.text(mapEntry.getDirectoryRoot().getFileName().toString(), NamedTextColor.AQUA));
        player.sendMessage(message);
        player.closeInventory();
        player.getInventory().setItemStack(0, ItemStack.AIR);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlying(true);
        player.setFlying(true);

        this.setupDataService.add(player.getUuid(), data);

        player.setTag(SETUP_TAG, 1);
        getTeleportTask(() -> {
            data.teleport(player);
        }).schedule();
    }

    private @NotNull Task.Builder getTeleportTask(@NotNull Runnable runnable) {
        return MinecraftServer.getSchedulerManager().buildTask(runnable).delay(3, ChronoUnit.SECONDS);
    }
}
