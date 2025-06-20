package net.theevilreaper.bounce.util;

import de.icevizion.api.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtil {

    private final Scoreboard scoreboard;
    private Objective objective;

    public ScoreboardUtil() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public void initLobbyScoreboard(String mapName) {
        objective = scoreboard.registerNewObjective("Lobby", "Dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§3Bounce");
        objective.getScore("§0").setScore(3);
        objective.getScore("§eMap§7:").setScore(2);
        objective.getScore("§7» §a" + mapName).setScore(1);
    }

    public void initGameScoreboard() {
        objective.unregister();
        objective = scoreboard.registerNewObjective("InGame", "Dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§7Loading§c.§6.§a.");
    }

    public void updateGameScoreboardDisplayName(int time) {
        objective.setDisplayName("§bKnockouts §7| " + TimeUtil.getTimeString(time));
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}