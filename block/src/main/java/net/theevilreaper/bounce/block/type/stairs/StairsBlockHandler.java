package net.theevilreaper.bounce.block.type.stairs;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class StairsBlockHandler implements BlockHandler {

    private final Block block;

    public StairsBlockHandler(Block block) {
        this.block = block;
    }

    @Override
    public Key getKey() {
        return this.block.key();
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.String("facing"),
                Tag.String("half"),
                Tag.String("shape"),
                Tag.Boolean("waterlogged")
        );
    }
}
