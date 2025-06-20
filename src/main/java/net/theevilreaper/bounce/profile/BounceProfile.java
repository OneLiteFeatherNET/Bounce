package net.theevilreaper.bounce.profile;

import net.theevilreaper.bounce.Bounce;
import net.theevilreaper.bounce.map.BounceMap;
import net.theevilreaper.bounce.util.PlayerJumpRunnable;
import de.icevizion.api.profile.PlayerProfile;
import de.icevizion.api.title.TitleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class BounceProfile extends PlayerProfile {

    private Player lastDamager;
    private int points, kills, deaths;
    private PlayerJumpRunnable jumpRunnable;

    public BounceProfile(Player player) {
        super(player);
    }

    public void registerJumpRunnable(Bounce game, BounceMap bounceMap) {
        this.jumpRunnable = new PlayerJumpRunnable(bounceMap, this);
        this.jumpRunnable.runTaskTimer(game, 0,3);
    }

    public void setLastDamager(Player paramPlayer) {
        this.lastDamager = paramPlayer;
    }

    public void addKill() {
        ++kills;
    }

    public void addDeath() {
        ++deaths;
    }

    public void addPoints(int paramPoints) {
        points += paramPoints;
        addKill();
        updateScoreboard();
        TitleManager.sendActionBar(getPlayer(), "§e+ §6" + paramPoints + " §7Punkte");
    }

    public void removePoints(int paramPoints) {
        addDeath();
        if (points > 0) {
            points -= paramPoints;
            updateScoreboard();
            TitleManager.sendActionBar(getPlayer(), "§c- §6" + paramPoints + " §7Punkte");
        }
    }

    public void sendStats(boolean winner) {
        getPlayer().sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        getPlayer().sendMessage("");
        getPlayer().sendMessage("§eStats dieser Runde");
        getPlayer().sendMessage(winner ? "§7Gewonnen: §a✔" : "§7Gewonnen: §c✘");
        getPlayer().sendMessage("§7Punkte: §c" + points);
        getPlayer().sendMessage("§7Kills: §c" + kills);
        getPlayer().sendMessage("§7Tode: §c" + deaths);
        getPlayer().sendMessage("");
        getPlayer().sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private void updateScoreboard() {
        getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(getPlayer().getDisplayName()).setScore(points);
    }

    public void resetDamager() {
        this.lastDamager = null;
    }

    public PlayerJumpRunnable getJumpRunnable() {
        return jumpRunnable;
    }

    public Player getLastDamager() {
        return lastDamager;
    }

    public int getPoints() {
        return points;
    }
}