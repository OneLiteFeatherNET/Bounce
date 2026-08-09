package net.theevilreaper.bounce.timer;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.aves.util.functional.VoidConsumer;
import net.theevilreaper.bounce.attribute.AttributeHelper;
import net.theevilreaper.bounce.util.ItemUtil;
import net.theevilreaper.xerus.api.phase.TimedPhase;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class TeleportPhase extends TimedPhase {

    private final PlayerConsumer teleport;
    private final VoidConsumer startTrigger;

    public TeleportPhase(PlayerConsumer teleport, VoidConsumer startTrigger) {
        super("Teleport", ChronoUnit.SECONDS, 1);
        this.setCurrentTicks(3);
        this.teleport = teleport;
        this.startTrigger = startTrigger;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.startTrigger.apply();
    }

    @Override
    public void onUpdate() {
    }

    @Override
    protected void onFinish() {
        Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player onlinePlayer : onlinePlayers) {
            ItemUtil.setItem(onlinePlayer);
            teleport.accept(onlinePlayer);
            AttributeHelper.disableJumpStrength(onlinePlayer);
        }
    }
}