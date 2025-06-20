package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minestom.server.entity.Player;
import net.theevilreaper.bounce.common.config.GameConfig;
import net.theevilreaper.bounce.common.util.Messages;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GameMessages extends Messages {

    private static final Component LEAVE_PART;
    private static final Component JOIN_PART;

    public static final Component ALREADY_FORCE_STARTED;
    public static final Component PHASE_NOT_RUNNING;
    public static final Component PHASE_FORCE_STARTED;

    static {
        LEAVE_PART = withMini("<gray>left the game!");
        JOIN_PART = withMini("<gray>joined the game!");

        int forceStartTime = GameConfig.FORCE_START_TIME;
        ALREADY_FORCE_STARTED = withMiniPrefix("<red>The game has already been force started!");
        PHASE_NOT_RUNNING = withMiniPrefix("<red>The lobby countdown is not running!");
        PHASE_FORCE_STARTED = withMiniPrefix("<gray>The timer has been set to <color:#09ff00><seconds></color> seconds!",
                TagResolver.builder().tag("seconds", (argumentQueue, context) -> Tag.preProcessParsed(String.valueOf(forceStartTime))).build());
    }

    @Contract
    public static @NotNull Component getLobbyTime(int time) {
        return withMiniPrefix("<gold>Starting in... <red>" + time);
    }

    @Contract
    public static @NotNull Component getStopTime(int time) {
        return  withMiniPrefix("<gold>Stopping in... <red>" + time);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component getJoinMessage(@NotNull Player player) {
        return withPrefix(withMini("<color:#249D9F>" + player.getUsername() + "</color>"))
                .append(Component.space()).append(JOIN_PART);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Component getLeaveMessage(@NotNull Player player) {
        return withPrefix(withMini("<color:#249D9F>" + player.getUsername() + "</color>"))
                .append(Component.space()).append(LEAVE_PART);
    }

    private GameMessages() {

    }
}
