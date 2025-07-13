package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GameMapBuilder {

    private final List<String> authors;
    private final PushData.Builder pushDataBuilder;
    private String name;
    private Pos spawn;
    private Pos gameSpawn;

    public GameMapBuilder() {
        this.pushDataBuilder = PushData.builder();
        this.pushDataBuilder
                .add(PushEntry.groundEntry(Block.GLASS, 1))
                .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 1))
                .add(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1))
                .add(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1));
        this.authors = new ArrayList<>();
    }

    public GameMapBuilder(@NotNull GameMap gameMap) {
        this.name = gameMap.getName();
        if (gameMap.getBuilders() != null) {
            this.authors = new ArrayList<>(List.of(gameMap.getBuilders()));
        } else {
            this.authors = new ArrayList<>();
        }
        this.spawn = gameMap.getSpawn();
        this.gameSpawn = gameMap.getGameSpawn();

        if (gameMap.getPushData() == null) {
            this.pushDataBuilder = PushData.builder();
            this.pushDataBuilder
                    .add(PushEntry.groundEntry(Block.GLASS, 1))
                    .add(PushEntry.pushEntry(Block.GOLD_BLOCK, 1))
                    .add(PushEntry.pushEntry(Block.DIAMOND_BLOCK, 1))
                    .add(PushEntry.pushEntry(Block.EMERALD_BLOCK, 1));
        } else{
            this.pushDataBuilder = PushData.builder(gameMap.getPushData());
        }
    }

    /**
     * Sets the name of the map.
     *
     * @param name the name of the map
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Adds authors to the map.
     *
     * @param authors the authors to add
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder addAuthors(String... authors) {
        this.authors.addAll(List.of(authors));
        return this;
    }

    /**
     * Sets the ground block for the map.
     *
     * @param groundBlock the block to set as the ground block
     * @return this builder instance for chaining
     */
    public @NotNull GameMapBuilder setGroundBlock(Block groundBlock) {
        PushEntry pushEntry = this.pushDataBuilder.getPushValues().getFirst();
        pushEntry.setBlock(groundBlock);
        return this;
    }

    public @NotNull GameMapBuilder addAuthor(String author) {
        if (!this.authors.contains(author)) {
            this.authors.add(author);
        }
        return this;
    }

    public @NotNull GameMapBuilder setSpawn(Pos spawn) {
        this.spawn = spawn;
        return this;
    }

    public @NotNull GameMapBuilder setGameSpawn(Pos gameSpawn) {
        this.gameSpawn = gameSpawn;
        return this;
    }

    /**
     * Builds a new {@link GameMap} instance with the current properties.
     *
     * @return a new GameMap instance
     */
    public @NotNull GameMap build() {
        return new GameMap(this.name, this.spawn, this.gameSpawn, pushDataBuilder.build());
    }

    /**
     * Returns the name of the map.
     *
     * @return the name of the map
     */
    public String getName() {
        return name;
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
     * Returns the spawn position of the map.
     *
     * @return the spawn position
     */
    public Pos getSpawn() {
        return spawn;
    }

    /**
     * Returns the list of authors for the map.
     *
     * @return the list of authors
     */
    public @NotNull List<String> getAuthors() {
        return authors;
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
