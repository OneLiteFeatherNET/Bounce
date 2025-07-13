package net.theevilreaper.bounce.setup.event.push;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class PlayerPushUpdateEvent implements PlayerEvent {

    private final  Player player;
    private final Material material;
    private final double push;

    public PlayerPushUpdateEvent(@NotNull Player player, @NotNull Material material, double push) {
        this.player = player;
        this.material = material;
        this.push = push;
    }


    public @NotNull Material getMaterial() {
        return material;
    }

    public double getPush() {
        return push;
    }

    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }
}
