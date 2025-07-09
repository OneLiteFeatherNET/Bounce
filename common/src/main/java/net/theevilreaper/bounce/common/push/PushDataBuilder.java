package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class PushDataBuilder implements PushData.Builder {

    private final List<PushEntry> blocks;

    public PushDataBuilder() {
        this.blocks = new ArrayList<>();
    }

    public PushDataBuilder(@NotNull PushData pushData) {
        this.blocks = new ArrayList<>();

        pushData.push().forEach((block, aDouble) -> this.blocks.add(new PushEntry(block, aDouble.intValue())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData.@NotNull Builder add(@NotNull Block block, double value) {
        this.blocks.add(new PushEntry(block, ((int) value)));
        return this;
    }

    @Override
    public PushData.@NotNull Builder add(int slot, @NotNull Block block, double value) {
        this.blocks.add(slot, new PushEntry(block, ((int) value)));
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
    public @NotNull @UnmodifiableView List<PushEntry> getPushValues() {
        return Collections.unmodifiableList(this.blocks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PushData build() {
        return new PushData(this.blocks.stream()
                .collect(Collectors.toMap(
                        PushEntry::getBlock,
                        pushEntry -> ((double) pushEntry.getValue()),
                        (v1, v2) -> v1,
                        HashMap::new
                ))
        );// in case of duplicate keys, keep the first
    }
}
