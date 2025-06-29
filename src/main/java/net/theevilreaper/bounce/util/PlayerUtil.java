package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.projectile.FireworkRocketMeta;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.FireworkExplosion;
import net.minestom.server.item.component.FireworkList;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.profile.ProfileService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlayerUtil {

    private final ProfileService profileService;
    private final PushData pushData;

    private Task task;

    public PlayerUtil(@NotNull ProfileService profileService, @NotNull PushData pushData) {
        this.profileService = profileService;
        this.pushData = pushData;
    }

    public void preparePlayers() {
        for (Player onlinePlayer : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.setLevel(0);
            BounceProfile profile = this.profileService.add(onlinePlayer);
            profile.registerJumpRunnable(pushData);
        }
    }

    public void playFirework(@NotNull Player player) {
        FireworkExplosion fireworkExplosion = new FireworkExplosion(
                FireworkExplosion.Shape.LARGE_BALL,
                List.of(NamedTextColor.AQUA),
                List.of(),
                false,
                false
        );
        task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                if (i == 5) {
                    task.cancel();
                    task = null;
                    return;
                }

                Entity firework = new Entity(EntityType.FIREWORK_ROCKET);
                firework.editEntityMeta(FireworkRocketMeta.class, meta -> {
                    meta.setFireworkInfo(ItemStack.of(Material.FIREWORK_ROCKET)
                            .with(ItemComponent.FIREWORKS, new FireworkList(
                                    (byte) 0, List.of(fireworkExplosion))));
                });

                firework.triggerStatus((byte) 17);
                MinecraftServer.getSchedulerManager().scheduleNextTick(firework::remove);
                ++i;
            }
        }).repeat(TaskSchedule.millis(500)).schedule();
    }

    public void broadcastWinner() {
        BounceProfile winnerProfile = this.profileService.getWinner();
        if (winnerProfile == null) return;

        Player player = winnerProfile.getPlayer();
        playFirework(player);
        for (BounceProfile profile : profileService.getProfileMap().values()) {
            boolean isWinner = profile.equals(winnerProfile);
            profile.sendStats(isWinner);
            var title = Title.title(player.getCustomName(), Component.text("wons the game", NamedTextColor.GRAY), Title.DEFAULT_TIMES);
            player.sendTitlePart(TitlePart.TITLE, title.title());
            player.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
            player.sendTitlePart(TitlePart.TIMES, title.times());
        }
    }
}
