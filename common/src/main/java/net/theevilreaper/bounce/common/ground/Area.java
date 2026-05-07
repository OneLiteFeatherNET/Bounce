package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;

/**
 * The {@link Area} interface represents an area in the game.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Area {

    void calculatePositions();

    /**
     * Returns a boolean indicator if the are includes an amount of positions.
     *
     * @return true when yes otherwise false
     */
    boolean hasPositions();

    /**
     * Returns the minimum point of the area.
     *
     * @return the point as {@link Vec}
     */
    Vec min();

    /**
     * Returns the maximum point of the area.
     *
     * @return the point as {@link Vec}
     */
    Vec max();

    /**
     * Returns the push data associated with this area.
     *
     * @return the push data as {@link PushData}
     */
    PushData data();

    /**
     * Returns the ground block of this area.
     *
     * @return the ground block
     */
    Block groundBlock();
}
