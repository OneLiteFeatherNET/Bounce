package net.theevilreaper.bounce.common.ground;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;

public interface Area {

    void calculatePositions();

    /**
     * Returns the minimum point of the area.
     *
     * @return the point as {@link Vec}
     */
    @NotNull Vec min();

    /**
     * Returns the maximum point of the area.
     *
     * @return the point as {@link Vec}
     */
    @NotNull Vec max();

    /**
     * Returns the push data associated with this area.
     *
     * @return the push data as {@link PushData}
     */
    @NotNull PushData data();

    /**
     * Returns the ground block of this area.
     *
     * @return the ground block
     */
    @NotNull Block groundBlock();
}
