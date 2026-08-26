package net.theevilreaper.bounce.setup.listener.map;

import net.onelitefeather.guira.event.SetupFinishEvent;
import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.data.BounceData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SetupFinishListener implements Consumer<SetupFinishEvent> {

    private final PlayerConsumer instanceSwitcher;
    private final OptionalSetupDataGetter setupDataRemover;

    public SetupFinishListener(@NotNull PlayerConsumer instanceSwitcher, @NotNull OptionalSetupDataGetter setupDataRemover) {
        this.instanceSwitcher = instanceSwitcher;
        this.setupDataRemover = setupDataRemover;
    }

    @Override
    public void accept(@NotNull SetupFinishEvent event) {
        BounceData setupData = ((BounceData) event.getData());

        this.instanceSwitcher.accept(setupData.getPlayer());
        setupData.reset();
        this.setupDataRemover.get(setupData.getId());
    }
}
