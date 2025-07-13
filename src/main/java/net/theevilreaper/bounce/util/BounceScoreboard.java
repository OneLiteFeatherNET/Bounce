package net.theevilreaper.bounce.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.theevilreaper.aves.util.Strings;
import net.theevilreaper.aves.util.TimeFormat;
import net.theevilreaper.bounce.common.util.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BounceScoreboard {

    private static final Component GAME_TITLE = Component.text("Knockouts", NamedTextColor.AQUA)
            .append(Component.space())
            .append(Component.text("|", NamedTextColor.GRAY))
            .append(Component.space());
    private final Sidebar sideBar;

    public BounceScoreboard() {
        sideBar = new Sidebar(Component.empty());
    }

    public void initLobbyLayout(@NotNull String mapName) {
        sideBar.setTitle(Component.text("Bounce", NamedTextColor.DARK_AQUA));

        sideBar.createLine(new Sidebar.ScoreboardLine("header", Component.space(), 3));
        Component mapLine = Component.text("Map", NamedTextColor.YELLOW)
                .append(Component.text(":", NamedTextColor.GRAY));
        sideBar.createLine(new Sidebar.ScoreboardLine("map_header", mapLine, 2));
        Component mapComponent = Messages.SEPARATOR
                .append(Component.text(mapName, NamedTextColor.GREEN));
        sideBar.createLine(new Sidebar.ScoreboardLine("map_name", mapComponent, 1));
    }

    public void initGameScoreboard() {
        sideBar.removeLine("header");
        sideBar.removeLine("map_header");
        sideBar.removeLine("map_name");

        // Create a new objective for the game scoreboard
        Component title = Component.text()
                .append(Component.text("Loading", NamedTextColor.GRAY))
                .append(Component.text(".", NamedTextColor.RED))
                .append(Component.text(".", NamedTextColor.GOLD))
                .append(Component.text(".", NamedTextColor.GREEN))
                .build();
        sideBar.setTitle(title);
    }

    /**
     * Adds the given player to the scoreboard viewers.
     *
     * @param player the player to add as a viewer
     */
    public void addViewer(@NotNull Player player) {
        this.sideBar.addViewer(player);
    }

    /**
     * Removes the given player from the scoreboard viewers.
     *
     * @param player the player to remove from the viewers
     */
    public void removeViewer(@NotNull Player player) {
        this.sideBar.removeViewer(player);
    }

    public void createPlayerLine(@NotNull Player player) {
        this.sideBar.createLine(new Sidebar.ScoreboardLine(player.getUuid().toString(), player.getDisplayName(), 0));
    }

    public void updatePlayerLine(@NotNull UUID uuid, int points) {
        System.out.printf("Updating player line for UUID: %s with points: %d%n", uuid, points);
        this.sideBar.updateLineScore(uuid.toString(), points);

        Sidebar.ScoreboardLine line = this.sideBar.getLine(uuid.toString());
        System.out.printf("Current line: %s%n", line);
    }

    /**
     * Updates the game scoreboard display name with the given time.
     *
     * @param time the time in seconds to display
     */
    public void updateGameScoreboardDisplayName(int time) {
        sideBar.setTitle(GAME_TITLE.append(Component.text(Strings.getTimeString(TimeFormat.MM_SS, time))));
    }
}