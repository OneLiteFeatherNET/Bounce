package net.theevilreaper.bounce.listener.game;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.sound.SoundEvent;
import net.theevilreaper.bounce.event.PlayerPowerUpPickupEvent;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.powerup.PowerUpType;
import net.theevilreaper.bounce.util.GameMessages;

import java.util.function.Consumer;

public final class PowerUpPickupListener implements Consumer<PlayerPowerUpPickupEvent> {

    private static final Sound PICKUP_SOUND = Sound.sound(SoundEvent.ENTITY_PLAYER_LEVELUP, Sound.Source.PLAYER, 1f, 1.5f);
    private static final Sound INK_SOUND = Sound.sound(SoundEvent.ENTITY_SQUID_SQUIRT, Sound.Source.PLAYER, 1f, 1f);

    @Override
    public void accept(PlayerPowerUpPickupEvent event) {
        if (!(event.getPlayer() instanceof BouncePlayer bouncePlayer) || !bouncePlayer.isRoundActive()) return;

        bouncePlayer.playSound(PICKUP_SOUND);
        if (event.getType() == PowerUpType.BLOOPER) {
            inkOpponents(bouncePlayer);
            return;
        }

        bouncePlayer.activatePowerUp(event.getType());
        bouncePlayer.sendMessage(GameMessages.getPowerUpComponent(event.getType()));
    }

    private void inkOpponents(BouncePlayer inker) {
        Instance instance = inker.getInstance();
        if (instance == null) return;

        Potion blindness = new Potion(PotionEffect.BLINDNESS, 0, PowerUpType.BLOOPER.getDurationTicks());
        for (Player player : instance.getPlayers()) {
            if (player == inker || !(player instanceof BouncePlayer opponent) || !opponent.isRoundActive()) continue;
            opponent.addEffect(blindness);
            opponent.playSound(INK_SOUND);
            opponent.sendMessage(GameMessages.getInkedComponent(inker));
        }
        inker.sendMessage(GameMessages.getBlooperComponent(PowerUpType.BLOOPER.getDurationSeconds()));
    }
}
