package net.theevilreaper.bounce.block.type;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class CandleBlockHandler implements BlockHandler {

    private final Key key;

    public CandleBlockHandler(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.Integer("candles"),
                Tag.Boolean("lit"),
                Tag.Boolean("waterlogged")
        );
    }
}
