package net.theevilreaper.bounce.map;

import de.icevizion.game.api.map.BaseMap;
import org.bukkit.Location;

public class BounceMap extends BaseMap {

    private final Location gameSpawn;
    private final int minPlayers;
    private final int maxPlayers;
    private final double defaultPush;
    private final double emeraldPush;
    private final double ironPush;
    private final double goldPush;

    public BounceMap(String name, String builders, Location spawn, Location gameSpawn, int minPlayers, int maxPlayers, double defaultPush, double emeraldPush, double ironPush, double goldPush) {
        super(name, builders, spawn);
        this.gameSpawn = gameSpawn;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.defaultPush = defaultPush;
        this.emeraldPush = emeraldPush;
        this.ironPush = ironPush;
        this.goldPush = goldPush;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
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

    public Location getGameSpawn() {
        return gameSpawn;
    }
}