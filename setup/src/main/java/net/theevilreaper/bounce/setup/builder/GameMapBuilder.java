package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.aves.map.BaseMapBuilder;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GameMapBuilder extends BaseMapBuilder {

    private static final int DEFAULT_SHUFFLE_INTERVAL_TICKS = 100;
    private static final double DEFAULT_RESHUFFLE_PERCENTAGE = 0.1;

    private final PushData.Builder pushDataBuilder;
    private @Nullable Pos gameSpawn;
    private @Nullable Area area;
    private int shuffleIntervalTicks;
    private double reshufflePercentage;
    private @Nullable Vec pos1;
    private @Nullable Vec pos2;

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

    public GameMapBuilder(GameMap gameMap) {
        super(gameMap);
        this.gameSpawn = gameMap.getGameSpawn();
        this.area = gameMap.getArea();
        this.shuffleIntervalTicks = gameMap.getShuffleIntervalTicks() > 0
                ? gameMap.getShuffleIntervalTicks()
                : DEFAULT_SHUFFLE_INTERVAL_TICKS;
        this.reshufflePercentage = gameMap.getReshufflePercentage() > 0
                ? gameMap.getReshufflePercentage()
                : DEFAULT_RESHUFFLE_PERCENTAGE;

        if (this.area != null) {
            this.pos1 = this.area.min();
            this.pos2 = this.area.max();
        }

        if (gameMap.getPushData() == null) {
            this.pushDataBuilder = PushData.builder();
            this.pushDataBuilder
                    .add(PushEntry.groundEntry(Block.GLASS, 1, 1.0))
                    .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 1, 0.05))
                    .add(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1, 0.03))
                    .add(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1, 0.02));
        } else {
            this.pushDataBuilder = PushData.builder(gameMap.getPushData());
        }
    }

    /**
     * Sets the ground block for the map.
     *
     * @param groundBlock the block to set as the ground block
     * @return this builder instance for chaining
     */
    public GameMapBuilder groundBlock(Block groundBlock) {
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
    public GameMapBuilder gameSpawn(Pos gameSpawn) {
        this.gameSpawn = gameSpawn;
        return this;
    }

    /**
     * Sets the ground area which gets dynamically filled.
     *
     * @param area the area, or {@code null} to disable dynamic filling
     * @return this builder instance for chaining
     */
    public GameMapBuilder area(@Nullable Area area) {
        this.area = area;
        return this;
    }

    /**
     * Sets the amount of ticks between two runtime reshuffles of the area.
     *
     * @param shuffleIntervalTicks the interval in ticks
     * @return this builder instance for chaining
     */
    public GameMapBuilder shuffleIntervalTicks(int shuffleIntervalTicks) {
        this.shuffleIntervalTicks = shuffleIntervalTicks;
        return this;
    }

    /**
     * Sets the fraction of the area's positions to re-roll on each runtime reshuffle.
     *
     * @param reshufflePercentage the percentage as a fraction between 0.0 and 1.0
     * @return this builder instance for chaining
     */
    public GameMapBuilder reshufflePercentage(double reshufflePercentage) {
        this.reshufflePercentage = reshufflePercentage;
        return this;
    }

    /**
     * Sets the first captured corner of the ground area.
     *
     * @param pos1 the corner position, or {@code null} to clear it
     * @return this builder instance for chaining
     */
    public GameMapBuilder pos1(@Nullable Vec pos1) {
        this.pos1 = pos1;
        return this;
    }

    /**
     * Sets the second captured corner of the ground area.
     *
     * @param pos2 the corner position, or {@code null} to clear it
     * @return this builder instance for chaining
     */
    public GameMapBuilder pos2(@Nullable Vec pos2) {
        this.pos2 = pos2;
        return this;
    }

    /**
     * Builds a new {@link GameMap} instance with the current properties.
     *
     * @return a new GameMap instance
     */
    @Override
    public GameMap build() {
        return new GameMap(this.name, this.spawn, this.gameSpawn, pushDataBuilder.build(), this.builders, this.area, this.shuffleIntervalTicks, this.reshufflePercentage);
    }

    /**
     * Returns the spawn position used during the game.
     *
     * @return the game spawn position
     */
    public @Nullable Pos getGameSpawn() {
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
     * Returns the first captured corner of the ground area.
     *
     * @return the corner position, or {@code null} if not set
     */
    public @Nullable Vec getPos1() {
        return pos1;
    }

    /**
     * Returns the second captured corner of the ground area.
     *
     * @return the corner position, or {@code null} if not set
     */
    public @Nullable Vec getPos2() {
        return pos2;
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
    public PushEntry getGroundBlockEntry() {
        return this.pushDataBuilder.getPushValues().getFirst();
    }

    /***
     * Returns the spawn position of the map or a default.
     *
     * @param defaultSpawn as fallback
     * @return the spawn position
     */
    public Pos getSpawnOrDefault(Pos defaultSpawn) {
        return this.spawn != null ? this.spawn : defaultSpawn;
    }

    /**
     * Returns whether every field required to save this map is present.
     *
     * @return {@code true} if {@link #getMissingFieldNames()} is empty
     */
    public boolean isReadyToSave() {
        return getMissingFieldNames().isEmpty();
    }

    /**
     * Returns the human-readable names of the required fields which are not yet set.
     *
     * @return an empty list if the map is ready to save
     */
    public List<String> getMissingFieldNames() {
        List<String> missing = new ArrayList<>();
        if (isDefaultName()) missing.add("Name");
        if (getSpawn() == null) missing.add("Spawn");
        if (getGameSpawn() == null) missing.add("Game Spawn");
        if (getArea() == null) missing.add("Area");
        if (!hasValidPushData()) missing.add("Push Data");
        return missing;
    }

    /**
     * Checks whether the push data is usable: every entry needs a positive value, and at least one non-ground
     * entry needs a positive weight so something can actually be placed besides the ground block.
     *
     * @return {@code true} if the push data satisfies both conditions
     */
    private boolean hasValidPushData() {
        boolean hasWeightedPushEntry = false;
        for (PushEntry entry : pushDataBuilder.getPushValues()) {
            if (entry.getValue() <= 0) return false;
            if (!entry.isGround() && entry.getWeight() > 0) hasWeightedPushEntry = true;
        }
        return hasWeightedPushEntry;
    }
}
