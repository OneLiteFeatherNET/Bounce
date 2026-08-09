package net.theevilreaper.bounce.profile;

import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.map.GameMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Manages player profiles ({@link BounceProfile}) during the lifecycle of the game.
 *
 * <p>Provides concurrent lookup, creation, removal, lifecycle startup triggers,
 * winner determination based on profile score, and cleanup methods.</p>

 * @author theEvilReaper
 * @version 1.1.0
 * @since 0.1.0
 */
public class ProfileService {

    private final Map<UUID, BounceProfile> profileMap;

    /**
     * Constructs a new {@link ProfileService} backed by a concurrent hash map.
     */
    public ProfileService() {
        this.profileMap = new ConcurrentHashMap<>();
    }

    /**
     * Adds or retrieves the {@link BounceProfile} for the specified player.
     *
     * @param player whose profile should be retrieved or created
     * @return existing or newly created profile
     */
    public BounceProfile add(Player player) {
        return this.profileMap.computeIfAbsent(player.getUuid(), uuid -> new BounceProfile(player));
    }

    /**
     * Removes the profile associated with the specified player.
     *
     * @param player whose profile should be removed
     * @return removed profile, or {@code null} if no profile was stored
     */
    public @Nullable BounceProfile remove(Player player) {
        return this.profileMap.remove(player.getUuid());
    }

    /**
     * Retrieves the profile associated with the specified player.
     *
     * @param player whose profile to retrieve
     * @return player's profile, or {@code null} if not found
     */
    public @Nullable BounceProfile get(Player player) {
        return this.profileMap.get(player.getUuid());
    }

    /**
     * Retrieves the profile associated with the specified UUID.
     *
     * @param uuid unique identifier of the player
     * @return player's profile, or {@code null} if not found
     */
    public @Nullable BounceProfile get(@Nullable UUID uuid) {
        if (uuid == null) return null;
        return this.profileMap.get(uuid);
    }

    /**
     * Starts the jump task for all stored profiles on the given map and invokes the consumer callback.
     *
     * @param gameMap  active game map
     * @param consumer action executed for each online player profile
     */
    public void start(GameMap gameMap, PlayerConsumer consumer) {
        for (BounceProfile value : this.profileMap.values()) {
            if (value.getJumpRunnable() != null) {
                value.getJumpRunnable().start(gameMap);
            }
            consumer.accept(value.getPlayer());
        }
    }

    /**
     * Determines and returns the winning profile (the profile with the highest points).
     *
     * @return winning profile, or {@code null} if no profiles are registered
     */
    public @Nullable BounceProfile getWinner() {
        if (this.profileMap.isEmpty()) return null;

        return profileMap.values().stream()
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    /**
     * Clears all stored player profiles.
     */
    public void clear() {
        if (this.profileMap.isEmpty()) return;
        this.profileMap.clear();
    }

    /**
     * Executes the given callback consumer for every profile and clears the profile map.
     *
     * @param callback action to execute for each profile before clearing
     */
    public void clear(Consumer<BounceProfile> callback) {
        if (this.profileMap.isEmpty()) return;
        for (BounceProfile value : this.profileMap.values()) {
            callback.accept(value);
        }
        this.profileMap.clear();
    }

    /**
     * Returns an unmodifiable view of the internal profile map.
     *
     * @return unmodifiable map mapping player UUIDs to their {@link BounceProfile}
     */
    public Map<UUID, BounceProfile> getProfileMap() {
        return Collections.unmodifiableMap(profileMap);
    }
}
