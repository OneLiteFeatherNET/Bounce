package net.theevilreaper.bounce.block.type.lantern;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Represents the block handler for the lantern block.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public class LanternBlockHandler implements BlockHandler {

    private final Block block;

    /**
     * Constructs a new {@code LanternBlockHandler} with the specified block.
     *
     * @param block the block associated with this handler
     */
    LanternBlockHandler(Block block) {
        this.block = block;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Key getKey() {
        return block.key();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.Boolean("hanging"),
                Tag.Boolean("waterlogged")
        );
    }
}
