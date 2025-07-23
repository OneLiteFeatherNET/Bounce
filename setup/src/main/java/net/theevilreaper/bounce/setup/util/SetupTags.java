package net.theevilreaper.bounce.setup.util;

import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public final class SetupTags {

    public static final Tag<Integer> SETUP_TAG = Tag.Transient("bounce.setup");
    public static final Tag<Integer> PUSH_SLOT_INDEX = Tag.Integer("push_slot_index");

    private SetupTags() {
        // Prevent instantiation
    }
}
