package net.theevilreaper.bounce;

import io.github.togar2.pvp.events.EntityKnockbackEvent;
import io.github.togar2.pvp.events.FinalAttackEvent;
import io.github.togar2.pvp.events.FinalDamageEvent;
import io.github.togar2.pvp.feature.CombatFeatureSet;
import io.github.togar2.pvp.feature.CombatFeatures;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import net.theevilreaper.aves.map.provider.MapProvider;
import net.theevilreaper.bounce.block.BlockLoader;
import net.theevilreaper.bounce.block.BlockLoaderBuilder;
import net.theevilreaper.bounce.block.type.lantern.LanternBlockFactory;
import net.theevilreaper.bounce.commands.StartCommand;
import net.theevilreaper.bounce.common.ListenerHandling;
import net.theevilreaper.bounce.common.config.GameConfig;
import net.theevilreaper.bounce.common.config.GameConfigReader;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.event.GamePrepareEvent;
import net.theevilreaper.bounce.event.PlayerLavaEvent;
import net.theevilreaper.bounce.event.ScoreUpdateEvent;
import net.theevilreaper.bounce.listener.PlayerChatListener;
import net.theevilreaper.bounce.listener.PlayerConfigurationListener;
import net.theevilreaper.bounce.listener.PlayerJoinListener;
import net.theevilreaper.bounce.listener.PlayerQuitListener;
import net.theevilreaper.bounce.listener.damage.AttackListener;
import net.theevilreaper.bounce.listener.damage.DamageListener;
import net.theevilreaper.bounce.listener.damage.KnockbackListener;
import net.theevilreaper.bounce.listener.game.GameFinishListener;
import net.theevilreaper.bounce.listener.game.GamePrepareListener;
import net.theevilreaper.bounce.listener.game.PlayerLavaListener;
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
        this.phaseSeries = new LinearPhaseSeries<>("Game");
        this.profileService = new ProfileService();
        this.playerUtil = new PlayerUtil(this.profileService, ((BounceMapProvider) this.mapProvider).getActiveMap().getPushData());
        this.scoreboard = new BounceScoreboard();
        this.registerPhases();

        MinecraftServer.getSchedulerManager().buildShutdownTask(this::unload);
    }

    public void load() {
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        registerCancelListener(globalEventHandler);
        registerListener(globalEventHandler);
        registerGameListener(globalEventHandler);
        this.registerCommands();
        this.phaseSeries.start();
        this.scoreboard.initLobbyLayout(((BounceMapProvider) this.mapProvider).getMapName());

        CombatFeatureSet featureSet = CombatFeatures.empty()
                .add(CombatFeatures.VANILLA_ATTACK)
                .add(CombatFeatures.VANILLA_ATTACK_COOLDOWN)
                .add(CombatFeatures.VANILLA_DAMAGE)
                .add(CombatFeatures.VANILLA_KNOCKBACK)
                .add(CombatFeatures.VANILLA_ENCHANTMENT)
                .build();
        globalEventHandler.addChild(featureSet.createNode());
        loadBlocks();
    }

    public void unload() {
        profileService.clear();
        profileService = null;
    }

    private void registerPhases() {
        this.phaseSeries.add(new LobbyPhase(this.gameConfig.minPlayers(), this.gameConfig.lobbyTime()));
        this.phaseSeries.add(new TeleportPhase(this.itemUtil, ((BounceMapProvider) this.mapProvider)::teleportToGameSpawn, this.scoreboard::initGameScoreboard));
        this.phaseSeries.add(new PlayingPhase(this.scoreboard::updateGameScoreboardDisplayName, () -> {
            this.profileService.start(((BounceMapProvider) this.mapProvider).getActiveMap(), scoreboard::createPlayerLine);
        }));
        this.phaseSeries.add(new RestartPhase());
    }

    private void registerListener(@NotNull EventNode<Event> node) {
        node.addListener(AsyncPlayerConfigurationEvent.class, new PlayerConfigurationListener(
                this.phaseSeries::getCurrentPhase,
                this.mapProvider.getActiveInstance(),
                gameConfig.maxPlayers())
        );
        node.addListener(PlayerSpawnEvent.class, new PlayerJoinListener(
                this.phaseSeries::getCurrentPhase,
                this::handleGeneralJoin
        ));

        node.addListener(PlayerDisconnectEvent.class, new PlayerQuitListener(
                this.phaseSeries::getCurrentPhase, this::handleGameLeave
        ));

        node.addListener(GamePrepareEvent.class, new GamePrepareListener(this.playerUtil));
        node.addListener(PlayerChatEvent.class, new PlayerChatListener());
    }

    private void registerGameListener(@NotNull EventNode<Event> node) {
        node.addListener(BounceGameFinishEvent.class, new GameFinishListener(profileService));
        node.addListener(ScoreUpdateEvent.class, new ScoreUpdateListener(scoreboard::updatePlayerLine));
        node.addListener(FinalAttackEvent.class, new AttackListener(this.phaseSeries::getCurrentPhase));
        node.addListener(FinalDamageEvent.class, new DamageListener());
        node.addListener(EntityKnockbackEvent.class, new KnockbackListener(this.profileService::get));
        node.addListener(PlayerLavaEvent.class, new PlayerLavaListener(this.profileService::get, ((BounceMapProvider) this.mapProvider).getActiveMap()::getGameSpawn));
    }

    private void registerCommands() {
        MinecraftServer.getCommandManager().register(new StartCommand(this.phaseSeries::getCurrentPhase));
    }

    private void handleGameLeave(@NotNull Player player) {
        BounceProfile profile = this.profileService.remove(player);

        if (profile == null) return;

        profile.getJumpRunnable().cancel();
        this.scoreboard.removeViewer(player);
    }

    private void handleGeneralJoin(@NotNull Player player) {
        this.mapProvider.teleportToSpawn(player, false);
        this.scoreboard.addViewer(player);
    }

    private void loadBlocks() {
        BlockManager blockHandler = MinecraftServer.getBlockManager();
        BlockLoader blockLoader = BlockLoader.builder(blockHandler);
        blockLoader
                .torch()
                .candle()
                .barrel()
                .fenceGate()
                .ironBars()
                .grindStone()
                .lanternVariant(LanternBlockFactory.Type.LANTERN)
                .ironChain()
                .stairs()
                .slab()
                .flowerPot();
    }
}
