package net.theevilreaper.bounce.setup.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {

    private static final Component FULL_SERVER = Component.text("Unable to join because the server is full!", NamedTextColor.RED);
    private final Supplier<@NotNull Instance> instanceSupplier;

    public PlayerConfigurationListener(@NotNull Supplier<Instance> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public void accept(@NotNull AsyncPlayerConfigurationEvent event) {
        event.setSpawningInstance(instanceSupplier.get());
    }
}