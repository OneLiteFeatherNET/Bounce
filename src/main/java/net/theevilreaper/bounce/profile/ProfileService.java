package net.theevilreaper.bounce.profile;

import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.map.GameMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ProfileService {

    private final Map<UUID, BounceProfile> profileMap;

    public ProfileService() {
        profileMap = new ConcurrentHashMap<>();
    }

    public @NotNull BounceProfile add(@NotNull Player player) {
        return this.profileMap.computeIfAbsent(player.getUuid(), uuid -> new BounceProfile(player));
    }

    public BounceProfile remove(@NotNull Player player) {
        return this.profileMap.remove(player.getUuid());
    }

    public BounceProfile get(@NotNull Player player) {
        return this.profileMap.get(player.getUuid());
    }

    public BounceProfile get(@NotNull UUID uuid) {
        return this.profileMap.get(uuid);
    }

    public void start(@NotNull GameMap gameMap, @NotNull PlayerConsumer consumer) {
        for (BounceProfile value : this.profileMap.values()) {
            value.getJumpRunnable().start(gameMap);
            consumer.accept(value.getPlayer());
        }
    }

    public BounceProfile getWinner() {
        if (this.profileMap.isEmpty()) return null;

        return profileMap.values().stream()
                .min(Comparator.naturalOrder()) // because "highest points" sorts first
                .orElse(null);
    }

    public void clear() {
        if (this.profileMap.isEmpty()) return;
        this.profileMap.clear();
    }

    public void clear(@NotNull Consumer<BounceProfile> callback) {
        if (this.profileMap.isEmpty()) return;
        for (BounceProfile value : this.profileMap.values()) {
            callback.accept(value);
        }
    }

    public Map<UUID, BounceProfile> getProfileMap() {
        return Collections.unmodifiableMap(profileMap);
    }
}
