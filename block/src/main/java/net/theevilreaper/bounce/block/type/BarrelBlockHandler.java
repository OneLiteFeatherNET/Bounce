package net.theevilreaper.bounce.block.type;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class BarrelBlockHandler implements BlockHandler {

    @Override
    public Key getKey() {
        return Block.BARREL.key();
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.String("facing"),
                Tag.Boolean("open")
        );
    }
}
