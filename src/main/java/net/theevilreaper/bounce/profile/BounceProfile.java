package net.theevilreaper.bounce.profile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import net.theevilreaper.bounce.jump.PlayerJumpTask;
import net.theevilreaper.bounce.util.GameMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class BounceProfile implements Comparable<BounceProfile> {

    private final Player player;
    private Player lastDamager;
    private int points;
    private int kills;
    private int deaths;
    private PlayerJumpTask jumpRunnable;
    private long firstReachTimestamp;

    public BounceProfile(@NotNull Player player) {
        this.player = player;
        this.points = 0;
    }

    public void registerJumpRunnable(@NotNull PushData pushData) {
        this.jumpRunnable = new PlayerJumpTask(player, pushData);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BounceProfile that)) return false;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(player);
    }

    public void addPoints(int paramPoints) {
        points += paramPoints;
        firstReachTimestamp = System.nanoTime();
        addKill();
        updateScoreboard();
        player.sendActionBar(GameMessages.getCoinComponent(paramPoints, true));
    }

    public void removePoints(int paramPoints) {
        addDeath();
        if (points > 0) {
            points -= paramPoints;
            updateScoreboard();
            player.sendActionBar(GameMessages.getCoinComponent(paramPoints, false));
        }
    }

    /**
     * Sends the round statistics to the player.
     *
     * @param winner true if the player won the round, false otherwise
     */
    public void sendStats(boolean winner) {
        Component message = GameMessages.STATS_LINE
                .append(Component.newline())
                .append(Component.text("Round statistics", NamedTextColor.YELLOW))
                .append(Component.newline())
                .append(Component.text("Win: ", NamedTextColor.GRAY)
                        .append(Component.text(winner ? "✔" : "✘", winner ? NamedTextColor.GREEN : NamedTextColor.RED)))
                .append(Component.newline())
                .append(Component.text("Points: ", NamedTextColor.GRAY)
                        .append(Component.text(points, NamedTextColor.RED)))
                .append(Component.newline())
                .append(Component.text("Kills: ", NamedTextColor.GRAY)
                        .append(Component.text(kills, NamedTextColor.RED)))
                .append(Component.newline())
                .append(Component.text("Deaths: ", NamedTextColor.GRAY)
                        .append(Component.text(deaths, NamedTextColor.RED)))
                .append(Component.newline())
                .append(GameMessages.STATS_LINE);

        this.player.sendMessage(message);
    }

    @Override
    public int compareTo(@NotNull BounceProfile other) {
        int pointComparison = Integer.compare(other.points, this.points); // Descending by points
        if (pointComparison != 0) return pointComparison;

        return Long.compare(this.firstReachTimestamp, other.firstReachTimestamp); // Ascending by timestamp
    }

    /**
     * Updates the scoreboard for this profile's player.
     */
    private void updateScoreboard() {
        EventDispatcher.call(new ScoreUpdateEvent(player, points));
    }

    /**
     * Resets the last damager of this profile.
     */
    public void resetDamager() {
        this.lastDamager = null;
    }

    /**
     * Returns the player associated with this profile.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the jump runnable associated with this profile's player.
     *
     * @return the jump runnable, or null if it has not been registered
     */
    public PlayerJumpTask getJumpRunnable() {
        return jumpRunnable;
    }

    /**
     * Returns the last player who damaged this profile's player.
     *
     * @return the last damager, or null if no player has damaged this profile's player
     */
    public @Nullable Player getLastDamager() {
        return lastDamager;
    }
}