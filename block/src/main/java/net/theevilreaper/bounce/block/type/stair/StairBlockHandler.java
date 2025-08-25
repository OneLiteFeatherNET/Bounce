package net.theevilreaper.bounce.block.type.stair;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class StairBlockHandler implements BlockHandler {

    private final Block block;

    public StairBlockHandler(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public @NotNull Key getKey() {
        return block.key();
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.String("facing"),
                Tag.String("half"),
                Tag.String("shape"),
                Tag.Boolean("waterlogged")
        );
    }
}
