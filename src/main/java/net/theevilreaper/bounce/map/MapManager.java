package net.theevilreaper.bounce.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.theevilreaper.bounce.Bounce;
import de.icevizion.api.adapter.LocationTypeAdapter;
import de.icevizion.api.file.JsonFileLoader;
import de.icevizion.game.api.map.MapService;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.Optional;

public class MapManager extends MapService {

    private final Bounce game;
    private final Gson gson;
    private BounceMap bounceMap;

    public MapManager(Bounce game) {
        this.game = game;
        this.gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Location.class, new LocationTypeAdapter()).create();
        loadMaps();
    }

    @Override
    public void loadMap(String s) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void loadMaps() {
        final Optional<BounceMap> map = JsonFileLoader.load(new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder(), "map.json"), BounceMap.class, gson);
        if (map.isPresent()) {
            bounceMap = map.get();
            game.getScoreboardUtil().initLobbyScoreboard(bounceMap.getName());
        } else {
            Bukkit.getConsoleSender().sendMessage("§cMap konnte nicht geladen werden");
        }
    }

    public BounceMap getMap() {
        return bounceMap;
    }
}