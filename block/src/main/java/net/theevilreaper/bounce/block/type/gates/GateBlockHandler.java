package net.theevilreaper.bounce.block.type.gates;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class GateBlockHandler implements BlockHandler {

    private final Key key;

    public GateBlockHandler(Key key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.String("facing"),
                Tag.Boolean("in_wall"),
                Tag.Boolean("open"),
                Tag.Boolean("powered")
        );
    }
}
