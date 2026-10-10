package net.theevilreaper.bounce.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.theevilreaper.bounce.common.map.GameMap;
import net.theevilreaper.bounce.common.player.PermissionAwarePlayer;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.event.PlayerDeathBlockEvent;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import net.theevilreaper.bounce.powerup.PowerUpType;
import net.theevilreaper.bounce.util.GameMessages;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link net.minestom.server.entity.Player} implementation used for the actual game.
 * <p>
 * Besides {@link PermissionAwarePlayer}'s LuckPerms-backed permission checks, it carries all
 * per-round game state directly (bounce-pad jump check, score, kills/deaths, last damager)
 * instead of a separate profile object looked up through a registry. The jump logic runs from
 * {@link #tick(long)} rather than a per-player scheduled {@link net.minestom.server.timer.Task}.
 * Round-scoped state only applies while {@link #isRoundActive()} is {@code true}, which is set by
 * {@link #startRound()} and cleared by {@link #endRound()}.
 */
public final class BouncePlayer extends PermissionAwarePlayer {

    private static final long PUSH_COOLDOWN_MS = 200; // 200 ms cooldown
    private static final double SUPER_JUMP_MULTIPLIER = 1.5;

    private boolean jumping;
    private @Nullable GameMap map;
    private @Nullable PushData pushData;
    private Block lastBlockBelow = Block.AIR; // Track previous block below the player
    private long lastPushTime = 0;

    private boolean roundActive;
    private int points;
    private int kills;
    private int deaths;
    private @Nullable Player lastDamager;
    private long firstReachTimestamp;
    private final Map<PowerUpType, Long> activePowerUps = new EnumMap<>(PowerUpType.class);

    public BouncePlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    /**
     * Enables the bounce-pad jump check for this player on the given map.
     *
     * @param bounceMap the map whose push blocks/spawn should be used
     * @param pushData  the push data describing the bounce-pad strength per block
     */
    public void startJumping(GameMap bounceMap, PushData pushData) {
        this.map = bounceMap;
        this.pushData = pushData;
        this.lastBlockBelow = Block.AIR;
        this.jumping = true;
    }

    /**
     * Disables the bounce-pad jump check for this player.
     */
    public void stopJumping() {
        this.jumping = false;
        this.map = null;
        this.pushData = null;
    }

    @Override
    public void tick(long time) {
        super.tick(time);
        if (!this.activePowerUps.isEmpty()) {
            long aliveTicks = getAliveTicks();
            this.activePowerUps.values().removeIf(expiry -> expiry <= aliveTicks);
        }
        if (!this.jumping) return;
        onJumpTick();
    }

    private void onJumpTick() {
        GameMap bounceMap = this.map;
        PushData data = this.pushData;
        if (bounceMap == null || data == null) return;

        Instance instance = getInstance();
        if (instance == null) return;

        int feetY = (int) Math.floor(getPosition().y());

        // Lava is not solid, so a fast falling player can end up inside it instead of above it
        for (int[] pos : getCheckPositions(feetY)) {
            if (isLava(instance.getBlock(pos[0], pos[1], pos[2]))) {
                lastBlockBelow = Block.LAVA;
                EventDispatcher.call(new PlayerLavaEvent(this));
                return;
            }
        }

        List<int[]> checkPositions = getCheckPositions(feetY - 1);
        Block foundJumpBlock = null;

        for (int[] pos : checkPositions) {
            Block block = instance.getBlock(pos[0], pos[1], pos[2]);

            if (block == Block.REDSTONE_BLOCK) {
                lastBlockBelow = block;
                EventDispatcher.call(new PlayerDeathBlockEvent(this));
                return;
            }

            if (isLava(block)) {
                lastBlockBelow = block;
                EventDispatcher.call(new PlayerLavaEvent(this));
                return;
            }

            if (data.hasBlock(block) && !block.compare(lastBlockBelow)) {
                foundJumpBlock = block;
            }
        }

        if (foundJumpBlock == null) {
            lastBlockBelow = Block.AIR;
            return;
        }

        if (getVelocity().y() < 0) {
            long now = System.currentTimeMillis();
            if (now - lastPushTime >= PUSH_COOLDOWN_MS) {
                double pushStrength = data.getPush(foundJumpBlock) * 10D;
                if (hasPowerUp(PowerUpType.SUPER_JUMP)) {
                    pushStrength *= SUPER_JUMP_MULTIPLIER;
                }
                Vec push = new Vec(0, pushStrength, 0); // Only push upwards
                setVelocity(push);
                lastPushTime = now;
            }
        }
        lastBlockBelow = foundJumpBlock;
    }

    private static boolean isLava(Block block) {
        // compare() ignores the block state, so flowing lava with a level property matches as well
        return block.compare(Block.LAVA);
    }

    private List<int[]> getCheckPositions(int y) {
        Pos playerPos = getPosition();
        double halfWidth = 0.3;
        double minX = playerPos.x() - halfWidth;
        double maxX = playerPos.x() + halfWidth;
        double minZ = playerPos.z() - halfWidth;
        double maxZ = playerPos.z() + halfWidth;

        return List.of(
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(playerPos.z())}, // center
                new int[]{(int) Math.floor(minX), y, (int) Math.floor(playerPos.z())},         // minX
                new int[]{(int) Math.floor(maxX), y, (int) Math.floor(playerPos.z())},         // maxX
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(minZ)},         // minZ
                new int[]{(int) Math.floor(playerPos.x()), y, (int) Math.floor(maxZ)}          // maxZ
        );
    }

    /**
     * Marks this player as an active participant of a new round and resets all round-scoped
     * state (score, kills, deaths, last damager).
     */
    public void startRound() {
        this.points = 0;
        this.kills = 0;
        this.deaths = 0;
        this.lastDamager = null;
        this.firstReachTimestamp = 0;
        clearPowerUps();
        this.roundActive = true;
    }

    /**
     * Ends this player's participation in the current round and stops the jump check.
     */
    public void endRound() {
        stopJumping();
        clearPowerUps();
        this.roundActive = false;
    }

    /**
     * Activates the given power-up for its configured duration. Picking up an already active power-up
     * restarts its duration.
     *
     * @param type the power-up to activate
     */
    public void activatePowerUp(PowerUpType type) {
        this.activePowerUps.put(type, getAliveTicks() + type.getDurationTicks());
        if (type == PowerUpType.SPEED) {
            addEffect(new Potion(PotionEffect.SPEED, 1, type.getDurationTicks()));
        }
    }

    /**
     * Returns whether the given power-up is currently active for this player.
     *
     * @param type the power-up to check
     * @return {@code true} if the power-up is active
     */
    public boolean hasPowerUp(PowerUpType type) {
        Long expiry = this.activePowerUps.get(type);
        return expiry != null && getAliveTicks() < expiry;
    }

    private void clearPowerUps() {
        if (this.activePowerUps.remove(PowerUpType.SPEED) != null) {
            removeEffect(PotionEffect.SPEED);
        }
        this.activePowerUps.clear();
    }

    /**
     * Returns whether this player is currently an active participant of a round.
     *
     * @return {@code true} if a round is active for this player
     */
    public boolean isRoundActive() {
        return roundActive;
    }

    /**
     * Sets the last player who damaged this player.
     *
     * @param paramPlayer the player who last damaged this player
     */
    public void setLastDamager(Player paramPlayer) {
        this.lastDamager = paramPlayer;
    }

    /**
     * Returns the last player who damaged this player.
     *
     * @return the last damager, or null if no player has damaged this player
     */
    public @Nullable Player getLastDamager() {
        return lastDamager;
    }

    /**
     * Resets the last damager of this player.
     */
    public void resetDamager() {
        this.lastDamager = null;
    }

    /**
     * Returns this player's current score.
     *
     * @return the current amount of points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Increments the kill count for this player.
     */
    public void addKill() {
        ++kills;
    }

    /**
     * Increments the death count for this player.
     */
    public void addDeath() {
        ++deaths;
    }

    /**
     * Adds points to this player's score and updates the scoreboard. The amount gets doubled while
     * {@link PowerUpType#DOUBLE_POINTS} is active.
     *
     * @param paramPoints the number of points to add
     */
    public void addPoints(int paramPoints) {
        if (hasPowerUp(PowerUpType.DOUBLE_POINTS)) {
            paramPoints *= 2;
        }
        points += paramPoints;
        firstReachTimestamp = System.nanoTime();
        addKill();
        updateScoreboard();
        sendActionBar(GameMessages.getCoinComponent(paramPoints, true));
    }

    /**
     * Removes points from this player's score.
     *
     * @param paramPoints the number of points to remove
     */
    public void removePoints(int paramPoints) {
        addDeath();
        if (points > 0) {
            points -= paramPoints;
            updateScoreboard();
            sendActionBar(GameMessages.getCoinComponent(paramPoints, false));
        }
    }

    /**
     * Sends the round statistics to this player.
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

        sendMessage(message);
    }

    /**
     * Compares two players by round standing: higher score wins, ties broken by
     * {@link #firstReachTimestamp} (later reach wins the tie).
     *
     * @param other the player to compare against
     * @return a negative, zero, or positive number as this player ranks below, equal to, or
     * above {@code other}
     */
    public int compareStanding(BouncePlayer other) {
        int pointComparison = Integer.compare(this.points, other.points);
        if (pointComparison != 0) return pointComparison;

        return Long.compare(this.firstReachTimestamp, other.firstReachTimestamp);
    }

    private void updateScoreboard() {
        EventDispatcher.call(new ScoreUpdateEvent(this, points));
    }
}
