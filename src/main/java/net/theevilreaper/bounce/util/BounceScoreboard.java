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

/**
 * The {@link BounceScoreboard} class is responsible for managing different layouts of the scoreboard during the game.
 * It provides methods to initialize the lobby layout, game scoreboard, and manage player lines.
 *
 * @version 1.0.0
 * @author theEvilReaper
 * @since 0.1.0
 */
public final class BounceScoreboard {

    private static final Component GAME_TITLE = Component.text("Knockouts", NamedTextColor.AQUA)
            .append(Component.space())
            .append(Component.text("|", NamedTextColor.GRAY))
            .append(Component.space());
    private final Sidebar sideBar;

    public BounceScoreboard() {
        sideBar = new Sidebar(Component.empty());
    }

    /**
     * Initializes the lobby layout of the scoreboard.
     * This method sets up the initial state of the scoreboard for the lobby.
     *
     * @param mapName the name of the map to display in the scoreboard
     */
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

    /**
     * Initializes the game scoreboard layout.
     * This method sets up the initial state of the scoreboard for the game.
     */
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

    /**
     * Creates a new line for the player in the scoreboard.
     * The player's UUID is used as the line identifier, and their display name is used as the line text.
     *
     * @param player the player for whom to create a line
     */
    public void createPlayerLine(@NotNull Player player) {
        this.sideBar.createLine(new Sidebar.ScoreboardLine(player.getUuid().toString(), player.getDisplayName(), 0));
    }

    /**
     * Updates the player's line score in the scoreboard.
     *
     * @param uuid   the UUID of the player whose score is to be updated
     * @param points the points to set for the player
     */
    public void updatePlayerLine(@NotNull UUID uuid, int points) {
        this.sideBar.updateLineScore(uuid.toString(), points);
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
