package net.theevilreaper.bounce.util;

import net.theevilreaper.bounce.map.BounceMap;
import net.theevilreaper.bounce.profile.BounceProfile;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerJumpRunnable extends BukkitRunnable {

    private final Player player;
    private final BounceMap bounceMap;
    private final BounceProfile bounceProfile;

    public PlayerJumpRunnable(BounceMap bounceMap, BounceProfile bounceProfile) {
        this.bounceMap = bounceMap;
        this.bounceProfile = bounceProfile;
        this.player = bounceProfile.getPlayer();
    }

    @Override
    public void run() {
        Material underPlayer = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        switch (underPlayer) {
            case STAINED_GLASS:
                player.setVelocity(new Vector(player.getVelocity().getX(), bounceMap.getDefaultPush(), player.getVelocity().getZ()));
                break;
            case REDSTONE_BLOCK:
                player.teleport(bounceMap.getGameSpawn());
                bounceProfile.removePoints(10);
                bounceProfile.resetDamager();
                break;
            case LAPIS_BLOCK:
                player.setVelocity(new Vector(player.getVelocity().getX(), bounceMap.getIronPush(), player.getVelocity().getZ()));
                break;
            case GOLD_BLOCK:
                player.setVelocity(new Vector(player.getVelocity().getX(), bounceMap.getGoldPush(), player.getVelocity().getZ()));
                break;
            case EMERALD_BLOCK:
                player.setVelocity(new Vector(player.getVelocity().getX(), bounceMap.getEmeraldPush(), player.getVelocity().getZ()));
                break;
            default:
                break;
        }
    }
}