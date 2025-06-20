package net.theevilreaper.bounce.listener;

import net.theevilreaper.bounce.map.MapManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerSpawnListener implements Listener {

    private final MapManager mapManager;

    public PlayerSpawnListener(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    @EventHandler
    public void on(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(mapManager.getMap().getSpawn());
    }
}
