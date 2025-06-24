package net.theevilreaper.bounce.common.ground;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class PushDataBuilder implements PushData.Builder {

    private final Map<Block, Double> blocks;

    public PushDataBuilder() {
        this.blocks = new HashMap<>();
    }

    public PushDataBuilder(@NotNull PushData pushData) {
        this.blocks = new HashMap<>(pushData.push());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData.@NotNull Builder add(@NotNull Block block, double value) {
        this.blocks.put(block, value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData.@NotNull Builder remove(@NotNull Block block) {
        this.blocks.remove(block);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PushData build() {
        return new PushData(this.blocks);
    }
}
