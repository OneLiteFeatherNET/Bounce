package net.theevilreaper.bounce;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.theevilreaper.bounce.commands.StartCommand;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.common.config.GameConfig;
import net.theevilreaper.bounce.common.config.GameConfigReader;
import net.theevilreaper.bounce.map.MapManager;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.timer.RestartPhase;
import net.theevilreaper.bounce.timer.TeleportPhase;
import net.theevilreaper.bounce.util.GameUtil;
import net.theevilreaper.bounce.util.ItemUtil;
import net.theevilreaper.bounce.util.ScoreboardUtil;
import net.theevilreaper.xerus.api.phase.LinearPhaseSeries;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Bounce implements ListenerHandling {

    private MapManager mapManager;
    private ProfileService<BounceProfile> profileService;
    private GameConfig gameConfig;
    private ScoreboardUtil scoreboardUtil;
    private GameUtil gameUtil;
    private ItemUtil itemUtil;
    private final LinearPhaseSeries<Phase> phaseSeries;

    public Bounce() {
        Path path = Paths.get("");
        this.gameConfig = new GameConfigReader(path.resolve("config")).getConfig();

        this.phaseSeries = new LinearPhaseSeries<>();


        this.registerCommands();
        this.registerPhases();
    }

    public void load() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(globalEventHandler);
        setPrefix("§7[§3Bounce§7] §r");
        WorldHelper.disablePlayerFileSave();
        scoreboardUtil = new ScoreboardUtil();
        mapManager = new MapManager(this);
        itemUtil = new ItemUtil();
        gameUtil = new GameUtil(this);
        profileService = new ProfileService<>();
        registerCommands();
        registerListener();
        lobbyPhase.start();
        // Cloud.getInstance().setSpigotState(SpigotState.AVAILABLE);
    }

    public void unload() {
        //Cloud.getInstance().setSpigotState(SpigotState.END);
        scoreboardUtil = null;
        mapManager = null;
        itemUtil = null;
        gameUtil = null;
        profileService.getProfiles().clear();
        profileService = null;
    }

    private void registerPhases() {
        this.phaseSeries.add(new LobbyPhase(this.gameConfig.minPlayers(), this.gameConfig.maxPlayers(), this.gameConfig.lobbyTime()));
        this.phaseSeries.add(new TeleportPhase(this.itemUtil, player -> { }));
        this.phaseSeries.add(new PlayingPhase(value -> {}));
        this.phaseSeries.add(new RestartPhase());
    }

    private void registerListener(@NotNull EventNode<Event> node) {
    }

    private void registerCommands() {
        MinecraftServer.getCommandManager().register(new StartCommand(this.phaseSeries::getCurrentPhase));
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

    public LobbyPhase getLobbyTimer() {
        return lobbyPhase;
    }

    public PlayingPhase getGameTimer() {
        return gameTimer;
    }

    public RestartPhase getRestartTimer() {
        return restartTimer;
    }
}
