package net.theevilreaper.bounce.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.aves.file.gson.KeyGsonAdapter;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import net.theevilreaper.bounce.common.adapter.AreaAdapter;
import net.theevilreaper.bounce.common.adapter.PushDataAdapter;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class GsonUtil {

    public static final Gson GSON;

    static {
        PositionGsonAdapter positionAdapter = new PositionGsonAdapter();
        GSON = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Pos.class, positionAdapter)
                .registerTypeAdapter(Vec.class, positionAdapter)
                .registerTypeAdapter(Key.class, KeyGsonAdapter.create())
                .registerTypeAdapter(Area.class, new AreaAdapter())
                .registerTypeAdapter(PushData.class, new PushDataAdapter())
                .create();
    }


    private GsonUtil() {

    }
}
