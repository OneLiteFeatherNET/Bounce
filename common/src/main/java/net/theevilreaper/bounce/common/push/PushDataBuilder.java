package net.theevilreaper.bounce.common.push;

import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link PushDataBuilder} class is used to build {@link PushData} instances.
 *
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 */
public final class PushDataBuilder implements PushData.Builder {

    private final List<PushEntry> blocks;

    /**
     * Creates a new {@link PushDataBuilder} instance.
     */
    public PushDataBuilder() {
        this.blocks = new ArrayList<>();
    }

    /**
     * Creates a new {@link PushDataBuilder} instance initialized with the provided {@link PushData}.
     *
     * @param pushData the existing {@link PushData} to initialize the builder with
     */
    public PushDataBuilder(PushData pushData) {
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
    public PushData.Builder add(PushEntry entry) {
        this.blocks.add(entry);
        return this;
    }

    @Override
    public PushData.Builder add(int index, PushEntry entry) {
        this.blocks.add(index, entry);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @UnmodifiableView List<PushEntry> getPushValues() {
        return Collections.unmodifiableList(this.blocks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PushData build() {
        return new PushData(this.blocks);
    }
}
