package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GameMap} contains all relevant information about a map which is used in the context of the game.
 * It holds data about the used positions and other things.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
@ApiStatus.NonExtendable
public final class GameMap extends BaseMap {

    private final Pos gameSpawn;
    private final PushData pushData;

    /**
     * Creates a new reference from the map class.
     *
     * @param name      the name of the map
     * @param spawn     the spawn position
     * @param gameSpawn the spawn position during the game
     * @param pushData  the {@link PushData} which includes information about push values
     */
    public GameMap(@NotNull String name, @NotNull Pos spawn, @NotNull Pos gameSpawn, @NotNull PushData pushData, @NotNull String... builders) {
        super(name, spawn, builders);
        this.gameSpawn = gameSpawn;
        this.pushData = pushData;

    }

    /**
     * Returns the given {@link PushData} reference.
     *
     * @return the reference
     */
    public @NotNull PushData getPushData() {
        return this.pushData;
    }

    /**
     * Returns the position which is used during the game.
     *
     * @return the game spawn position
     */
    public @NotNull Pos getGameSpawn() {
        return gameSpawn;
    }
}