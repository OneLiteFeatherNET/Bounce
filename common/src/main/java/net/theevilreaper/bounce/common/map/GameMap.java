package net.theevilreaper.bounce.common.map;

import net.minestom.server.coordinate.Pos;
import net.theevilreaper.aves.map.BaseMap;

public class GameMap extends BaseMap {

    private Pos gameSpawn;
    private double defaultPush;
    private double emeraldPush;
    private double ironPush;
    private double goldPush;

    public GameMap(String name, Pos spawn, Pos gameSpawn, double defaultPush, double emeraldPush, double ironPush, double goldPush) {
        super(name, spawn, "Team");
        this.gameSpawn = gameSpawn;
        this.defaultPush = defaultPush;
        this.emeraldPush = emeraldPush;
        this.ironPush = ironPush;
        this.goldPush = goldPush;
    }

    public void setDefaultPush(double defaultPush) {
        this.defaultPush = defaultPush;
    }

    public void setEmeraldPush(double emeraldPush) {
        this.emeraldPush = emeraldPush;
    }

    public void setGameSpawn(Pos gameSpawn) {
        this.gameSpawn = gameSpawn;
    }

    public void setGoldPush(double goldPush) {
        this.goldPush = goldPush;
    }

    public void setIronPush(double ironPush) {
        this.ironPush = ironPush;
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

    public Pos getGameSpawn() {
        return gameSpawn;
    }
}