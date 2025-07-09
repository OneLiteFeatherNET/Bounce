package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PushDataBuilder implements PushData.Builder {

    private final List<PushEntry> blocks;

    public PushDataBuilder() {
        this.blocks = new ArrayList<>();
    }

    public PushDataBuilder(@NotNull PushData pushData) {
        this.blocks = new ArrayList<>();
        for (int i = 0; i < pushData.push().size(); i++) {
            PushEntry entry = pushData.push().get(i);
            if (entry.isGround()) {
                this.blocks.add(PushEntry.groundEntry(entry.getBlock(), entry.getValue()));
            } else {
                this.blocks.add(PushEntry.pushEntry(entry.getBlock(), entry.getValue()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData.@NotNull Builder add(@NotNull PushEntry entry) {
        this.blocks.add(entry);
        return this;
    }

    @Override
    public PushData.@NotNull Builder add(int index, @NotNull PushEntry entry) {
        this.blocks.add(index, entry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData.@NotNull Builder updateBlock(int slot, @NotNull Block entry) {
        return null;
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
        return new PushData(this.blocks);
    }
}
