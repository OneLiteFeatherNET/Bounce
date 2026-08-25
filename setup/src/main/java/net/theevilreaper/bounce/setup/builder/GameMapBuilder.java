package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.aves.map.BaseMapBuilder;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GameMapBuilder extends BaseMapBuilder {

    private static final int DEFAULT_SHUFFLE_INTERVAL_TICKS = 100;
    private static final double DEFAULT_RESHUFFLE_PERCENTAGE = 0.1;

    private final PushData.Builder pushDataBuilder;
    private Pos gameSpawn;
    private @Nullable Area area;
    private int shuffleIntervalTicks;
    private double reshufflePercentage;

    public GameMapBuilder() {
        super();
        this.shuffleIntervalTicks = DEFAULT_SHUFFLE_INTERVAL_TICKS;
        this.reshufflePercentage = DEFAULT_RESHUFFLE_PERCENTAGE;
        this.pushDataBuilder = PushData.builder();
        this.pushDataBuilder
                .add(PushEntry.groundEntry(Block.GLASS, 1, 1.0))
                .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 1, 0.05))
                .add(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 0.03))
                .add(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1, 0.02));
    }

    public GameMapBuilder(@NotNull GameMap gameMap) {
        super(gameMap);
        this.gameSpawn = gameMap.getGameSpawn();
        this.area = gameMap.getArea();
        this.shuffleIntervalTicks = gameMap.getShuffleIntervalTicks() > 0
                ? gameMap.getShuffleIntervalTicks()
                : DEFAULT_SHUFFLE_INTERVAL_TICKS;
        this.reshufflePercentage = gameMap.getReshufflePercentage() > 0
                ? gameMap.getReshufflePercentage()
                : DEFAULT_RESHUFFLE_PERCENTAGE;

        if (gameMap.getPushData() == null) {
            this.pushDataBuilder = PushData.builder();
            this.pushDataBuilder
                    .add(PushEntry.groundEntry(Block.GLASS, 1, 1.0))
                    .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 1, 0.05))
                    .add(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 0.03))
                    .add(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1, 0.02));
        } else{
            this.pushDataBuilder = PushData.builder(gameMap.getPushData());
        }
    }

    /**
     * Sets the ground block for the map.
     *
     * @param groundBlock the block to set as the ground block
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder groundBlock(Block groundBlock) {
        PushEntry pushEntry = this.pushDataBuilder.getPushValues().getFirst();
        pushEntry.setBlock(groundBlock);
        return this;
    }

    /**
     * Sets the spawn position for the map.
     *
     * @param gameSpawn the spawn position
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder gameSpawn(Pos gameSpawn) {
        this.gameSpawn = gameSpawn;
        return this;
    }

    /**
     * Sets the ground area which gets dynamically filled.
     *
     * @param area the area, or {@code null} to disable dynamic filling
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder area(@Nullable Area area) {
        this.area = area;
        return this;
    }

    /**
     * Sets the amount of ticks between two runtime reshuffles of the area.
     *
     * @param shuffleIntervalTicks the interval in ticks
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder shuffleIntervalTicks(int shuffleIntervalTicks) {
        this.shuffleIntervalTicks = shuffleIntervalTicks;
        return this;
    }

    /**
     * Sets the fraction of the area's positions to re-roll on each runtime reshuffle.
     *
     * @param reshufflePercentage the percentage as a fraction between 0.0 and 1.0
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder reshufflePercentage(double reshufflePercentage) {
        this.reshufflePercentage = reshufflePercentage;
        return this;
    }

    /**
     * Builds a new {@link GameMap} instance with the current properties.
     *
     * @return a new GameMap instance
     */
    @Override
    public @NotNull GameMap build() {
        return new GameMap(this.name, this.spawn, this.gameSpawn, pushDataBuilder.build(), this.builders, this.area, this.shuffleIntervalTicks, this.reshufflePercentage);
    }

    /**
     * Returns the spawn position used during the game.
     *
     * @return the game spawn position
     */
    public Pos getGameSpawn() {
        return gameSpawn;
    }

    /**
     * Returns the ground area which gets dynamically filled.
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
     * Returns the fraction of the area's positions to re-roll on each runtime reshuffle.
     *
     * @return the percentage as a fraction between 0.0 and 1.0
     */
    public double getReshufflePercentage() {
        return reshufflePercentage;
    }

    /**
     * Returns the {@link PushData.Builder} instance used to build push data.
     *
     * @return the push data builder
     */
    public PushData.Builder getPushDataBuilder() {
        return pushDataBuilder;
    }

    /**
     * Returns the first push entry which is used as the ground block entry.
     *
     * @return the ground block entry
     */
    public @NotNull PushEntry getGroundBlockEntry() {
        return this.pushDataBuilder.getPushValues().getFirst();
    }

    public @NotNull Pos getSpawnOrDefault(@NotNull Pos defaultSpawn) {
        return this.spawn != null ? this.spawn : defaultSpawn;
    }
}
