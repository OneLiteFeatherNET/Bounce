package net.theevilreaper.bounce.profile;

import net.minestom.server.entity.Player;
import net.theevilreaper.aves.util.functional.PlayerConsumer;
import net.theevilreaper.bounce.common.map.GameMap;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ProfileService {

    private final Map<UUID, BounceProfile> profileMap;

    public ProfileService() {
        profileMap = new ConcurrentHashMap<>();
    }

    public BounceProfile add(Player player) {
        return this.profileMap.computeIfAbsent(player.getUuid(), uuid -> new BounceProfile(player));
    }

    public @Nullable BounceProfile remove(Player player) {
        return this.profileMap.remove(player.getUuid());
    }

    public @Nullable BounceProfile get(Player player) {
        return this.profileMap.get(player.getUuid());
    }

    public @Nullable BounceProfile get(UUID uuid) {
        return this.profileMap.get(uuid);
    }

    public void start(GameMap gameMap, PlayerConsumer consumer) {
        for (BounceProfile value : this.profileMap.values()) {
            value.getJumpRunnable().start(gameMap);
            consumer.accept(value.getPlayer());
        }
    }

    public @Nullable BounceProfile getWinner() {
        if (this.profileMap.isEmpty()) return null;

        return profileMap.values().stream()
                .min(Comparator.naturalOrder()) // because "highest points" sorts first
                .orElse(null);
    }

    public void clear() {
        if (this.profileMap.isEmpty()) return;
        this.profileMap.clear();
    }

    public void clear(Consumer<BounceProfile> callback) {
        if (this.profileMap.isEmpty()) return;
        for (BounceProfile value : this.profileMap.values()) {
            callback.accept(value);
        }
    }

    public Map<UUID, BounceProfile> getProfileMap() {
        return Collections.unmodifiableMap(profileMap);
    }
}
