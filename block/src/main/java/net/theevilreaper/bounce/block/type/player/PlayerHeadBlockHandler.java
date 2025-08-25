package net.theevilreaper.bounce.block.type.player;

import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;

import java.util.Collection;
import java.util.List;

public class PlayerHeadBlockHandler implements BlockHandler {

    @Override
    public Key getKey() {
        return Block.PLAYER_HEAD.key();
    }

    @Override
    public Collection<Tag<?>> getBlockEntityTags() {
        return List.of(
                Tag.Boolean("powered"),
                Tag.Integer("rotation")
        );
    }
}
