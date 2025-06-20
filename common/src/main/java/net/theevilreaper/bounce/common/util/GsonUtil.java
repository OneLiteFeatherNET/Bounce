package net.theevilreaper.bounce.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.theevilreaper.aves.file.gson.PositionGsonAdapter;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class GsonUtil {

    public static Gson GSON;

    static {
        PositionGsonAdapter positionAdapter = new PositionGsonAdapter();
        GSON = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Pos.class, positionAdapter)
                .registerTypeAdapter(Vec.class, positionAdapter)
                .create();
    }


    private GsonUtil() {

    }
}
