package net.theevilreaper.bounce.listener.damage;

import io.github.togar2.pvp.events.FinalDamageEvent;
import io.github.togar2.pvp.player.CombatPlayer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.registry.DynamicRegistry;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.profile.BounceProfile;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class DamageListener implements Consumer<FinalDamageEvent> {

    private final Function<Player, BounceProfile> profileFunction;
    private final PlayerConsumer spawnConsumer;

    public DamageListener(@NotNull Function<Player, BounceProfile> profileFunction, @NotNull PlayerConsumer spawnConsumer) {
        this.profileFunction = profileFunction;
        this.spawnConsumer = spawnConsumer;
    }

    @Override
    public void accept(@NotNull FinalDamageEvent event) {
        if (event.getEntity().getEntityType() != EntityType.PLAYER) return;

        DynamicRegistry.Key<DamageType> type = event.getDamage().getType();
        Player originPlayer = (Player) event.getEntity();
        CombatPlayer player = (CombatPlayer) event.getEntity();

        if (type == DamageType.LAVA) {
            player.setVelocityNoUpdate(vec -> Vec.ZERO);
            BounceProfile profile = profileFunction.apply(originPlayer);
            if (profile == null) return;
            profile.removePoints(5);
            if (profile.getLastDamager() != null) {
                BounceProfile damagerProfile = profileFunction.apply(profile.getLastDamager());
                damagerProfile.addPoints(10);
                profile.resetDamager();
            }
            this.spawnConsumer.accept(originPlayer);
        }

        if (type == DamageType.ON_FIRE || type == DamageType.IN_FIRE) {
            originPlayer.setFireTicks(0);
        }

        event.setInvulnerabilityTicks(100);
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