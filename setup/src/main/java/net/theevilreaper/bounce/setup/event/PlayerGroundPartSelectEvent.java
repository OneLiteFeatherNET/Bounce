package net.theevilreaper.bounce.setup.event;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class PlayerGroundPartSelectEvent implements PlayerEvent {

    private final Player player;
    private final Material material;
    private final GroundPart part;

    public PlayerGroundPartSelectEvent(@NotNull Player player, @NotNull Material material, @NotNull GroundPart part) {
        this.player = player;
        this.material = material;
        this.part = part;
    }

    public @NotNull GroundPart getPart() {
        return part;
    }

    public @NotNull Material getMaterial() {
        return this.material;
    }

    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }

    public enum GroundPart {
        BLOCK,
        FIRST_PUSH,
        SECOND_PUSH,
        THIRD_PUSH,
    }
}
