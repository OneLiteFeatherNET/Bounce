package net.theevilreaper.bounce.setup.builder;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GameMapBuilder {

    private final List<String> authors;
    private final PushData.Builder pushDataBuilder;
    private String name;
    private Pos spawn;
    private Pos gameSpawn;
    private Block groundBlock;

    public GameMapBuilder() {
        this.pushDataBuilder = PushData.builder();
        this.pushDataBuilder
                .add(Block.RED_STAINED_GLASS, 0.5)
                .add(Block.ORANGE_STAINED_GLASS, 0.5)
                .add(Block.YELLOW_STAINED_GLASS, 0.5);
        this.authors = new ArrayList<>();
        this.groundBlock = Block.GLASS;

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
        this.pushDataBuilder = PushData.builder();
        this.groundBlock = Block.GLASS;
    }

    public @NotNull GameMapBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public @NotNull GameMapBuilder addAuthors(String... authors) {
        this.authors.addAll(List.of(authors));
        return this;
    }

    public @NotNull GameMapBuilder setGroundBlock(Block groundBlock) {
        this.groundBlock = groundBlock;
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

    public @NotNull GameMapBuilder addPush(@NotNull Block block, double push) {
        this.pushDataBuilder.add(block, push);
        return this;
    }

    public @NotNull GameMapBuilder removePush(@NotNull Block block) {
        this.pushDataBuilder.remove(block);
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

    public String getName() {
        return name;
    }

    public Pos getGameSpawn() {
        return gameSpawn;
    }

    public Pos getSpawn() {
        return spawn;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public PushData.Builder getPushDataBuilder() {
        return pushDataBuilder;
    }

    public @NotNull Block getGroundBlock() {
        return groundBlock;
    }

    public @NotNull Pos getSpawnOrDefault(@NotNull Pos defaultSpawn) {
        return this.spawn != null ? this.spawn : defaultSpawn;
    }
}
