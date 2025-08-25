package net.theevilreaper.bounce.block.type;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockSoundType;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class LanternBlockHandler implements BlockHandler {

    public static final Key KEY = BlockSoundType.LANTERN.key();

    @Override
    public Key getKey() {
        return BlockSoundType.LANTERN.key();
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.Boolean("hanging"),
                Tag.Boolean("waterlogged")
        );
    }
}
