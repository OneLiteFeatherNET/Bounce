package net.theevilreaper.bounce.setup.util;

import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public final class SetupTags {

    public static final Tag<Integer> SETUP_TAG = Tag.Transient("bounce.setup");
    public static final Tag<Void> LOADING_TAG = Tag.Transient("bounce.setup.loading");
    public static final Tag<Integer> PUSH_BLOCK_SELECT = Tag.Integer("bounce.push_block_select");

    private SetupTags() {
        // Prevent instantiation
    }
}
