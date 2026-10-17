package net.theevilreaper.bounce.common.push;

import net.minestom.server.property.ServerProperties;

/**
 * The {@link PushPhysics} class contains the values which are used to convert a push value into an upwards velocity.
 * It also allows to estimate how high a player flies with a given push value, which is used to preview a push block in the setup.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.10.0
 */
public final class PushPhysics {

    /**
     * The multiplier which converts a push value into a velocity in blocks per second.
     */
    public static final double VELOCITY_MULTIPLIER = 10D;

    /**
     * The gravity which the client applies to a player each tick.
     */
    public static final double PLAYER_GRAVITY = 0.08;

    /**
     * The vertical air resistance which the client applies to a player each tick.
     */
    public static final double PLAYER_AIR_RESISTANCE = 0.98;

    /**
     * Returns the upwards velocity in blocks per second for the given push value.
     *
     * @param pushValue the push value of a block
     * @return the velocity in blocks per second
     */
    public static double velocity(double pushValue) {
        return pushValue * VELOCITY_MULTIPLIER;
    }

    /**
     * Returns the upwards velocity in blocks per tick for the given push value.
     *
     * @param pushValue the push value of a block
     * @return the velocity in blocks per tick
     */
    public static double velocityPerTick(double pushValue) {
        return velocity(pushValue) / ServerProperties.SERVER_TICKS_PER_SECOND.get();
    }

    /**
     * Applies one tick of player gravity and air resistance to the given vertical velocity.
     *
     * @param velocityPerTick the current vertical velocity in blocks per tick
     * @return the vertical velocity in blocks per tick for the next tick
     */
    public static double nextVelocityPerTick(double velocityPerTick) {
        return (velocityPerTick - PLAYER_GRAVITY) * PLAYER_AIR_RESISTANCE;
    }

    /**
     * Estimates the height in blocks a player reaches when the given push value is applied.
     * The calculation mirrors the vertical movement of a player which is simulated by the client.
     *
     * @param pushValue the push value of a block
     * @return the estimated height in blocks
     */
    public static double peakHeight(double pushValue) {
        double velocityPerTick = velocityPerTick(pushValue);
        double height = 0;
        while (velocityPerTick > 0) {
            height += velocityPerTick;
            velocityPerTick = nextVelocityPerTick(velocityPerTick);
        }
        return height;
    }

    private PushPhysics() {
        // Nothing to do here
    }
}
