package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The {@link GameMap} contains all relevant information about a map which is used in the context of the game.
 * It holds data about the used positions and other things.
 *
 * @author theEvilReaper
 * @version 1.2.0
 * @since 0.1.0
 */
public final class GameMap extends BaseMap {

    private final Pos gameSpawn;
    private final PushData pushData;
    private final @Nullable Area area;
    private final int shuffleIntervalTicks;
    private final double reshufflePercentage;

    /**
     * Creates a new reference from the map class.
     *
     * @param name                 the name of the map
     * @param spawn                the spawn position
     * @param gameSpawn            the spawn position during the game
     * @param pushData             the {@link PushData} which includes information about push values
     * @param builders             the list of builders who worked on the map
     * @param area                 the ground area which gets dynamically filled, or {@code null} for a fully manual map
     * @param shuffleIntervalTicks the amount of ticks between two runtime reshuffles of the area
     * @param reshufflePercentage  the fraction (0.0-1.0) of the area's positions to re-roll on each reshuffle
     */
    public GameMap(String name, Pos spawn, Pos gameSpawn, PushData pushData, List<String> builders, @Nullable Area area, int shuffleIntervalTicks, double reshufflePercentage) {
        super(name, spawn, builders);
        this.gameSpawn = gameSpawn;
        this.pushData = pushData;
        this.area = area;
        this.shuffleIntervalTicks = shuffleIntervalTicks;
        this.reshufflePercentage = reshufflePercentage;
    }

    /**
     * Returns the given {@link PushData} reference.
     *
     * @return the reference
     */
    public PushData getPushData() {
        return this.pushData;
    }

    /**
     * Returns the position which is used during the game.
     *
     * @return the game spawn position
     */
    public Pos getGameSpawn() {
        return gameSpawn;
    }

    /**
     * Returns the dynamically filled ground area of this map, or {@code null} for a fully manual map.
     *
     * @return the area, or {@code null}
     */
    public @Nullable Area getArea() {
        return area;
    }

    /**
     * Returns the amount of ticks between two runtime reshuffles of the area.
     *
     * @return the interval in ticks
     */
    public int getShuffleIntervalTicks() {
        return shuffleIntervalTicks;
    }

    /**
     * Returns the fraction of the area's positions which get re-rolled on each runtime reshuffle.
     *
     * @return the percentage as a fraction between 0.0 and 1.0
     */
    public double getReshufflePercentage() {
        return reshufflePercentage;
    }
}
