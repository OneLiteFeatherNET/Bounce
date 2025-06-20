package net.theevilreaper.bounce;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.theevilreaper.aves.map.MapProvider;
import net.theevilreaper.bounce.commands.StartCommand;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.common.config.GameConfig;
import net.theevilreaper.bounce.common.config.GameConfigReader;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import net.theevilreaper.bounce.listener.PlayerConfigurationListener;
import net.theevilreaper.bounce.listener.PlayerJoinListener;
import net.theevilreaper.bounce.listener.PlayerQuitListener;
import net.theevilreaper.bounce.listener.game.GameFinishListener;
import net.theevilreaper.bounce.listener.game.ScoreUpdateListener;
import net.theevilreaper.bounce.map.BounceMapProvider;
import net.theevilreaper.bounce.profile.BounceProfile;
import net.theevilreaper.bounce.profile.ProfileService;
import net.theevilreaper.bounce.timer.PlayingPhase;
import net.theevilreaper.bounce.timer.LobbyPhase;
import net.theevilreaper.bounce.timer.RestartPhase;
import net.theevilreaper.bounce.timer.TeleportPhase;
import net.theevilreaper.bounce.util.ItemUtil;
import net.theevilreaper.bounce.util.PlayerUtil;
import net.theevilreaper.bounce.util.BounceScoreboard;
import net.theevilreaper.xerus.api.phase.LinearPhaseSeries;
import net.theevilreaper.xerus.api.phase.Phase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Bounce implements ListenerHandling {

    private ProfileService profileService;
    private final GameConfig gameConfig;
    private final BounceScoreboard scoreboard;
    private final ItemUtil itemUtil;
    private final MapProvider mapProvider;
    private final LinearPhaseSeries<Phase> phaseSeries;
    private final PlayerUtil playerUtil;

    public Bounce() {
        Path path = Paths.get("");
        this.gameConfig = new GameConfigReader(path.resolve("config")).getConfig();
        this.mapProvider = new BounceMapProvider(path);
        this.itemUtil = new ItemUtil();
        this.phaseSeries = new LinearPhaseSeries<>();
        this.profileService = new ProfileService();
        this.playerUtil = new PlayerUtil(this.profileService);
        this.scoreboard = new BounceScoreboard();
        this.registerPhases();
    }

    public void load() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(globalEventHandler);
        registerListener(globalEventHandler);
        registerGameListener(globalEventHandler);
        this.registerCommands();
        this.phaseSeries.start();
        this.scoreboard.initLobbyLayout(((BounceMapProvider) this.mapProvider).getMapName());
    }

    public void unload() {
        profileService.clear();
        profileService = null;
    }

    private void registerPhases() {
        this.phaseSeries.add(new LobbyPhase(this.gameConfig.minPlayers(), this.gameConfig.maxPlayers(), this.gameConfig.lobbyTime()));
        this.phaseSeries.add(new TeleportPhase(this.itemUtil, ((BounceMapProvider) this.mapProvider)::teleportToGameSpawn));
        this.phaseSeries.add(new PlayingPhase(value -> {
        }));
        this.phaseSeries.add(new RestartPhase());
    }

    private void registerListener(@NotNull EventNode<Event> node) {
        node.addListener(AsyncPlayerConfigurationEvent.class, new PlayerConfigurationListener(
                this.phaseSeries::getCurrentPhase,
                this.mapProvider.getActiveInstance(),
                gameConfig.maxPlayers())
        );
        node.addListener(PlayerSpawnEvent.class, new PlayerJoinListener(this.phaseSeries::getCurrentPhase, player -> {
            this.mapProvider.teleportToSpawn(player, true);
        }));

        node.addListener(PlayerDisconnectEvent.class, new PlayerQuitListener(
                this.phaseSeries::getCurrentPhase, this::handleGameLeave
        ));
    }

    private void registerGameListener(@NotNull EventNode<Event> node) {
        node.addListener(BounceGameFinishEvent.class, new GameFinishListener(playerUtil));
        node.addListener(ScoreUpdateEvent.class, new ScoreUpdateListener(scoreboard::updatePlayerLine));
    }

    private void registerCommands() {
        MinecraftServer.getCommandManager().register(new StartCommand(this.phaseSeries::getCurrentPhase));
    }

    private void handleGameLeave(@NotNull Player player) {
        BounceProfile profile = this.profileService.remove(player);

        if (profile == null) return;

        profile.getJumpRunnable().cancel();
    }
}
