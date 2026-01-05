package net.theevilreaper.bounce.block.type.grates;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class IronGrateBlockHandler implements BlockHandler {
    @Override
    public Key getKey() {
        return Material.IRON_BARS.key();
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.Boolean("east"),
                Tag.Boolean("north"),
                Tag.Boolean("south"),
                Tag.Boolean("west"),
                Tag.Boolean("waterlogged")
        );
    }
}
