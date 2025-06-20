package net.theevilreaper.bounce.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PlayerBlockListener implements Listener {

    @EventHandler
    public void on(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void on(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void on(BlockFadeEvent event) {
        event.setCancelled(true);
    }
}
