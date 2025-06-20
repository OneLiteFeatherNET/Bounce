package net.theevilreaper.bounce.timer;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.util.ItemUtil;
import net.theevilreaper.xerus.api.phase.TimedPhase;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class TeleportPhase extends TimedPhase {

    private final ItemUtil itemUtil;
    private final PlayerConsumer teleport;

    public TeleportPhase(@NotNull ItemUtil itemUtil, @NotNull PlayerConsumer teleport) {
        super("Teleport", ChronoUnit.SECONDS, 1);
        this.setCurrentTicks(3);
        this.itemUtil = itemUtil;
        this.teleport = teleport;
    }

    @Override
    public void onUpdate() {
        // Nothing to do here
    }

    @Override
    protected void onFinish() {
        Collection<@NotNull Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();
        for (Player onlinePlayer : onlinePlayers) {
            itemUtil.setItem(onlinePlayer);
            teleport.accept(onlinePlayer);
        }
    }
}