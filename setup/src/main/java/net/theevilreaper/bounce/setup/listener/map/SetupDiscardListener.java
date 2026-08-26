package net.theevilreaper.bounce.setup.listener.map;

import net.onelitefeather.guira.functional.OptionalSetupDataGetter;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.map.SetupDiscardEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SetupDiscardListener implements Consumer<SetupDiscardEvent> {

    private final PlayerConsumer instanceSwitcher;
    private final OptionalSetupDataGetter setupDataRemover;

    public SetupDiscardListener(@NotNull PlayerConsumer instanceSwitcher, @NotNull OptionalSetupDataGetter setupDataRemover) {
        this.instanceSwitcher = instanceSwitcher;
        this.setupDataRemover = setupDataRemover;
    }

    @Override
    public void accept(@NotNull SetupDiscardEvent event) {
        BounceData setupData = ((BounceData) event.getData());

        this.instanceSwitcher.accept(setupData.getPlayer());
        setupData.reset();
        this.setupDataRemover.get(setupData.getId());
    }
}
