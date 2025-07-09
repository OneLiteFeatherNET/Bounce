package net.theevilreaper.bounce.setup.listener;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {

    private final Supplier<@NotNull Instance> instanceSupplier;

    public PlayerConfigurationListener(@NotNull Supplier<Instance> instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
    }

    @Override
    public void accept(@NotNull AsyncPlayerConfigurationEvent event) {
        event.setSpawningInstance(instanceSupplier.get());
    }
}