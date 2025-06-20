package net.theevilreaper.bounce.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodListener implements Listener {

    @EventHandler
    public void on(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}