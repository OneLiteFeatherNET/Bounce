package net.theevilreaper.bounce;

import net.theevilreaper.bounce.commands.StartCommand;
import com.theEvilReaper.bounce.listener.*;
import net.theevilreaper.bounce.listener.*;
import net.theevilreaper.bounce.map.MapManager;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.timer.InGameTimer;
import net.theevilreaper.bounce.timer.LobbyTimer;
import net.theevilreaper.bounce.timer.RestartTimer;
import net.theevilreaper.bounce.util.GameUtil;
import net.theevilreaper.bounce.util.ItemUtil;
import net.theevilreaper.bounce.util.ScoreboardUtil;
import de.icevizion.api.profile.ProfileService;
import de.icevizion.api.world.WorldHelper;
import de.icevizion.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class Bounce extends Game {

    private MapManager mapManager;
    private ProfileService<BounceProfile> profileService;

    private ScoreboardUtil scoreboardUtil;
    private GameUtil gameUtil;
    private ItemUtil itemUtil;

    private LobbyTimer lobbyTimer;
    private InGameTimer gameTimer;
    private RestartTimer restartTimer;

    @Override
    public void load() {
        setPrefix("§7[§3Bounce§7] §r");
        WorldHelper.disablePlayerFileSave();
        updateGameRules();
        scoreboardUtil = new ScoreboardUtil();
        mapManager = new MapManager(this);
        itemUtil = new ItemUtil();
        gameUtil = new GameUtil(this);
        profileService = new ProfileService<>();
        registerTimer();
        registerCommands();
        registerListener();
        lobbyTimer.start();
       // Cloud.getInstance().setSpigotState(SpigotState.AVAILABLE);
    }

    @Override
    public void unload() {
        //Cloud.getInstance().setSpigotState(SpigotState.END);
        scoreboardUtil = null;
        mapManager = null;
        itemUtil = null;
        gameUtil = null;
        profileService.getProfiles().clear();
        profileService = null;
    }

    private void updateGameRules() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doFireTick", "false");
        }
    }

    private void registerTimer() {
        lobbyTimer = new LobbyTimer(this);
        gameTimer = new InGameTimer(this);
        restartTimer = new RestartTimer(this);
    }

    private void registerListener() {
        getServer().getPluginManager().registerEvents(new PlayerBlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDropListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerFoodListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSpawnListener(mapManager), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    private void registerCommands() {
        getCommand("start").setExecutor(new StartCommand(this));
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public GameUtil getGameUtil() {
        return gameUtil;
    }

    public ItemUtil getItemUtil() {
        return itemUtil;
    }

    public ScoreboardUtil getScoreboardUtil() {
        return scoreboardUtil;
    }

    public ProfileService<BounceProfile> getProfileService() {
        return profileService;
    }

    public LobbyTimer getLobbyTimer() {
        return lobbyTimer;
    }

    public InGameTimer getGameTimer() {
        return gameTimer;
    }

    public RestartTimer getRestartTimer() {
        return restartTimer;
    }
}
