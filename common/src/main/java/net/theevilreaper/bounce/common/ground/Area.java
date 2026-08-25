package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;

import java.util.List;

/**
 * The {@link Area} interface represents an area in the game.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
public interface Area {

    /**
     * Scans the volume between {@link #min()} and {@link #max()} in the given instance and records every position
     * whose block matches {@link #groundBlock()}. A no-op if positions were already calculated, see {@link #hasPositions()}.
     *
     * @param instance the instance to scan
     */
    void calculatePositions(Instance instance);

    /**
     * Returns a boolean indicator if the are includes an amount of positions.
     *
     * @return true when yes otherwise false
     */
    boolean hasPositions();

    /**
     * Returns the positions calculated by {@link #calculatePositions(Instance)}, or an empty list if it hasn't
     * been called yet.
     *
     * @return an unmodifiable view of the calculated positions
     */
    List<Vec> positions();

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
