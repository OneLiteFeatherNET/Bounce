package net.theevilreaper.bounce.listener;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.profile.BounceProfile;
import de.icevizion.game.api.GameState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private final Bounce game;

    public PlayerDamageListener(Bounce game) {
        this.game = game;
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!(event.getEntityType() == EntityType.PLAYER)) return;
        if (game.getGameState() != GameState.INGAME) {
            event.setCancelled(true);
        } else {
            Player player = (Player) event.getEntity();
            event.setCancelled(true);
            switch (event.getCause()) {
                case ENTITY_ATTACK:
                    event.setDamage(0);
                    event.setCancelled(false);
                    break;
                case FIRE_TICK:
                case FIRE:
                    player.setFireTicks(0);
                    break;
                case LAVA:
                    player.getVelocity().zero();
                    BounceProfile profile = game.getProfileService().getProfile(player).get();
                    profile.removePoints(5);
                    if (profile.getLastDamager() != null) {
                        game.getProfileService().getProfile(profile.getLastDamager()).get().addPoints(10);
                        profile.resetDamager();
                    }
                    player.teleport(game.getMapManager().getMap().getGameSpawn());
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event) {
        if (!(event.getEntityType() == EntityType.PLAYER) | game.getGameState() != GameState.INGAME) return;
        if (((event.getEntity() instanceof Player) && (event.getDamager() instanceof Player))) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            game.getProfileService().getProfile(player).get().setLastDamager(damager);
        }
    }
}