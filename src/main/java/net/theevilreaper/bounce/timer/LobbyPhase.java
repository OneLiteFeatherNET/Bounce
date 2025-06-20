package net.theevilreaper.bounce.timer;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.sound.SoundEvent;
import net.theevilreaper.xerus.api.phase.TickDirection;
import net.theevilreaper.xerus.api.phase.TimedPhase;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;

import static net.minestom.server.MinecraftServer.getConnectionManager;
import static net.theevilreaper.bounce.common.config.GameConfig.FORCE_START_TIME;

public class LobbyPhase extends TimedPhase {

    private static final Sound PLING = Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_BELL, Sound.Source.MASTER, 1.0f, 1.0f);
    private final int minPlayers;
    private final int maxPlayers;
    private final int lobbyPhaseTime;
    private boolean forceStarted;

    public LobbyPhase(int minPlayers, int maxPlayers, int lobbyPhaseTime) {
        super("Lobby", ChronoUnit.SECONDS, 1);
        this.setPaused(true);
        this.setCurrentTicks(30);
        this.setTickDirection(TickDirection.DOWN);
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyPhaseTime = lobbyPhaseTime;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void onUpdate() {
        setLevel();
       // this.timeUpdater.accept(getCurrentTicks());

       // GameMapProvider gameMapProvider = (GameMapProvider) this.mapProvider;
        switch (getCurrentTicks()) {
            case 30, 20, 3, 1 -> broadcastTime();
            case 10 -> {
                this.broadcastTime();
               // gameMapProvider.loadGameChunks();
            }
            case 5 -> {
                this.broadcastTime();
               // gameMapProvider.triggerSpawnPlacement();
            }
            default -> {
                // Nothing to do here
            }
        }
    }

    @Override
    protected void onFinish() {

    }

    private void setLevel() {
        this.setLevel(getCurrentTicks());
    }

    private void broadcastTime() {
        for (Player onlinePlayer : getConnectionManager().getOnlinePlayers()) {
        //    onlinePlayer.sendMessage(GameMessages.getLobbyTime(getCurrentTicks()));
            onlinePlayer.playSound(PLING, onlinePlayer.getPosition());
        }
    }

    private void setLevel(int amount) {
        if (amount < 0) return;
        float currentExpCount = (float) this.getCurrentTicks() / getLobbyOrForceTime();
        for (Player onlinePlayer : getConnectionManager().getOnlinePlayers()) {
            onlinePlayer.setLevel(amount);
            onlinePlayer.setExp(currentExpCount);
        }
    }

    /**
     * Updates some data to display the current time.
     *
     * @param player the player who should receive the update
     */
    public void updatePlayerValues(@NotNull Player player) {
        player.setLevel(getCurrentTicks());
        float currentExpCount = (float) this.getCurrentTicks() / getLobbyOrForceTime();
        player.setExp(currentExpCount);
    }

    /**
     * Updates the state which indicates, if the phase is force started or not.
     *
     * @param forceStarted true if the phase is force started otherwise false
     */
    public void setForceStarted(boolean forceStarted) {
        if (forceStarted) {
            this.setCurrentTicks(FORCE_START_TIME);
        }
        this.forceStarted = forceStarted;
    }

    public void checkStartCondition() {
        if (isPaused() && getConnectionManager().getOnlinePlayers().size() >= this.minPlayers) {
            this.setPaused(false);
        }
    }

    public void checkStopCondition() {
        if (getConnectionManager().getOnlinePlayers().size() - 1 <= this.maxPlayers) {
            this.setPaused(true);
            this.setCurrentTicks(this.lobbyPhaseTime);
            setLevel();
        }
    }

    private int getLobbyOrForceTime() {
        return isForceStarted() ? FORCE_START_TIME : this.lobbyPhaseTime;
    }

    public boolean isForceStarted() {
        return forceStarted;
    }
}

    /*@Override
    public void onStart() {
        game.setGameState(GameState.LOBBY);
    }

    @Override
    public void onEnd() {
        Bukkit.broadcastMessage(game.getPrefix() + "§eAlle Spieler werden in die Arena teleportiert");
        game.getGameUtil().preparePlayers();
        new TeleportTask(game).start();
    }
    
    @Override
    public void onTick() {
        if (game.getMapManager().getMap().getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TitleManager.sendActionBar(player, Messages.NOT_ENOUGH_PLAYERS.toString());
            }
            setTime(getStartTime());
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(getCurrentTime());
        }

        switch (getCurrentTime()) {
            case 10:
            case 3:
            case 2:
                Bukkit.broadcastMessage(game.getPrefix() + "§7Das Spiel startet in §3" + getCurrentTime() + " §7Sekunden");
                break;
            case 1:
                Bukkit.broadcastMessage(game.getPrefix() + "§7Das Spiel startet in §3" + getCurrentTime() + " §7Sekunde");
                break;
            default:
                break;
        }
    }

    @Override
    public void start() {
        this.start(game, 0, 20);
    }*/
