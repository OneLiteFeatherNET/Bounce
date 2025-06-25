package net.theevilreaper.bounce.common.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.key.Key;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.ground.Area;
import net.theevilreaper.bounce.common.ground.GroundArea;
import net.theevilreaper.bounce.common.push.PushData;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public final class AreaAdapter implements JsonSerializer<Area>, JsonDeserializer<Area> {
    @Override
    public @NotNull Area deserialize(
            @NotNull JsonElement element,
            @NotNull Type type,
            @NotNull JsonDeserializationContext context
    ) {
        JsonObject jsonObject = element.getAsJsonObject();

        Vec min = context.deserialize(jsonObject.get("min"), Vec.class);
        Vec max = context.deserialize(jsonObject.get("max"), Vec.class);
        PushData pushData = context.deserialize(jsonObject.get("data"), PushData.class);
        Key groundBlockKey = context.deserialize(jsonObject.get("groundBlock"), Key.class);

        Block groundBlock = Block.fromKey(groundBlockKey);

        if (groundBlock == null) {
            groundBlock = Block.GRASS_BLOCK; // Default to grass block if the key is invalid
        }

        return new GroundArea(min, max, groundBlock, pushData);
    }

    @Override
    public @NotNull JsonElement serialize(@NotNull Area area, @NotNull Type type, @NotNull JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("min", context.serialize(area.min(), Vec.class));
        jsonObject.add("max", context.serialize(area.max(), Vec.class));

        jsonObject.add("data", context.serialize(area.data(), PushData.class));

        Key key = area.groundBlock().key();
        jsonObject.add("groundBlock", context.serialize(key, Key.class));
        return jsonObject;
    }
}
