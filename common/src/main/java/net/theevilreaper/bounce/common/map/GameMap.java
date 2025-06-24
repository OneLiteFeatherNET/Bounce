package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;
import org.jetbrains.annotations.NotNull;

public class GameMap extends BaseMap {

    private final Pos gameSpawn;
    private final double defaultPush;
    private final double emeraldPush;
    private final double ironPush;
    private final double goldPush;

    public GameMap(String name, Pos spawn, Pos gameSpawn, double defaultPush, double emeraldPush, double ironPush, double goldPush) {
        super(name, spawn, "Team");
        this.gameSpawn = gameSpawn;
        this.defaultPush = defaultPush;
        this.emeraldPush = emeraldPush;
        this.ironPush = ironPush;
        this.goldPush = goldPush;
    }

    public double getDefaultPush() {
        return defaultPush;
    }

    public double getEmeraldPush() {
        return emeraldPush;
    }

    public double getGoldPush() {
        return goldPush;
    }

    public double getIronPush() {
        return ironPush;
    }

    public @NotNull Pos getGameSpawn() {
        return gameSpawn;
    }
}