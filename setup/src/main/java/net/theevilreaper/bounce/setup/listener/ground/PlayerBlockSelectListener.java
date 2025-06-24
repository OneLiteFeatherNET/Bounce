package net.theevilreaper.bounce.setup.listener.ground;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.setup.builder.GameMapBuilder;
import net.theevilreaper.bounce.setup.data.BounceData;
import net.theevilreaper.bounce.setup.event.PlayerBlockSelectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerBlockSelectListener implements Consumer<PlayerBlockSelectEvent> {

    private final Function<Player, Optional<BounceData>> bounceDataSupplier;

    public PlayerBlockSelectListener(@NotNull Function<Player, Optional<BounceData>> bounceDataSupplier) {
        this.bounceDataSupplier = bounceDataSupplier;
    }

    @Override
    public void accept(@NotNull PlayerBlockSelectEvent event) {
        Optional<BounceData> data = bounceDataSupplier.apply(event.getPlayer());

        if (data.isEmpty()) return;

        BounceData bounceData = data.get();
        GameMapBuilder mapBuilder = bounceData.getMapBuilder();
        Block block = event.getMaterial().block();

        switch (event.getPart()) {
            case BLOCK -> mapBuilder.setGroundBlock(block);
        }
    }
}
