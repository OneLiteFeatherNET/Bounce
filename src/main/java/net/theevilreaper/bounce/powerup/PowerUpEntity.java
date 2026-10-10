package net.theevilreaper.bounce.powerup;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.theevilreaper.bounce.event.PlayerPowerUpPickupEvent;
import net.theevilreaper.bounce.player.BouncePlayer;

/**
 * A floating power-up which bobs up and down at a fixed position.
 * <p>
 * The movement is purely visual: instead of teleporting the entity every tick, only the translation of the display
 * transformation gets toggled every {@link #HALF_PERIOD_TICKS} and the client interpolates between both states.
 * This keeps the movement smooth and the server position stable, which makes the pickup check trivial.
 * Once a round-active {@link BouncePlayer} touches the power-up, a {@link PlayerPowerUpPickupEvent} gets
 * dispatched and the entity removes itself.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public final class PowerUpEntity extends Entity {

    static final int HALF_PERIOD_TICKS = 20;
    private static final double AMPLITUDE = 0.25;
    private static final Vec UP = new Vec(0, AMPLITUDE, 0);
    private static final Vec DOWN = new Vec(0, -AMPLITUDE, 0);
    // Centered around the entity position and tall enough to cover the whole bobbing range
    private static final BoundingBox PICKUP_BOX = new BoundingBox(1.0, 1.0, 1.0, new Vec(-0.5, -0.5, -0.5));

    private final PowerUpType type;
    private boolean up;

    /**
     * Creates a new power-up entity for the given type.
     *
     * @param type the type which gets applied on pickup
     */
    public PowerUpEntity(PowerUpType type) {
        super(EntityType.ITEM_DISPLAY);
        this.type = type;
        setNoGravity(true);
        setGlowing(true);
        editEntityMeta(ItemDisplayMeta.class, meta -> {
            meta.setItemStack(type.getIcon());
            meta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.VERTICAL);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long time) {
        if (getAliveTicks() % HALF_PERIOD_TICKS == 0) {
            bob();
        }
        checkPickup();
    }

    private void bob() {
        this.up = !this.up;
        editEntityMeta(ItemDisplayMeta.class, meta -> {
            meta.setTransformationInterpolationStartDelta(0);
            meta.setTransformationInterpolationDuration(HALF_PERIOD_TICKS);
            meta.setTranslation(this.up ? UP : DOWN);
        });
    }

    private void checkPickup() {
        Instance instance = getInstance();
        if (instance == null) return;

        for (Player player : instance.getPlayers()) {
            if (!(player instanceof BouncePlayer bouncePlayer) || !bouncePlayer.isRoundActive()) continue;
            if (!PICKUP_BOX.intersectBox(getPosition().sub(player.getPosition()), player.getBoundingBox())) continue;

            EventDispatcher.call(new PlayerPowerUpPickupEvent(bouncePlayer, this.type));
            remove();
            return;
        }
    }

    /**
     * Returns the type of this power-up.
     *
     * @return the type
     */
    public PowerUpType getType() {
        return type;
    }
}
