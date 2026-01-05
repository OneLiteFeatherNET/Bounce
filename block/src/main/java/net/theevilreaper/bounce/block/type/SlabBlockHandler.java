package net.theevilreaper.bounce.block.type;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class SlabBlockHandler implements BlockHandler {

    private final Key key;

    public SlabBlockHandler(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.String("type"),
                Tag.Boolean("waterlogged")
        );
    }
}
