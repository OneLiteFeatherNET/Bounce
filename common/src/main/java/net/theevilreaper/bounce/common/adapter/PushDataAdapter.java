package net.theevilreaper.bounce.common.adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.kyori.adventure.key.Key;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushData;
import net.theevilreaper.bounce.common.push.PushEntry;

import java.lang.reflect.Type;

/**
 * Serializer and Deserializer implementation for {@link PushData} object.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class PushDataAdapter implements JsonDeserializer<PushData>, JsonSerializer<PushData> {

    @Override
    public PushData deserialize(JsonElement element, Type type, JsonDeserializationContext context) {
        JsonArray jsonArray = element.getAsJsonArray();
        PushData.Builder builder = PushData.builder();

        if (jsonArray.isEmpty()) {
            return builder.build(); // Return empty PushData if no entries are present
        }

        for (JsonElement jsonElement : jsonArray.asList()) {
            Key blockKey = context.deserialize(jsonElement.getAsJsonObject().get("block"), Key.class);
            int value = jsonElement.getAsJsonObject().get("value").getAsInt();
            boolean ground = jsonElement.getAsJsonObject().get("ground").getAsBoolean();
            Block block = Block.fromKey(blockKey);

            if (ground) {
                builder.add(0, PushEntry.groundEntry(block, value));
            } else {
                builder.add(PushEntry.pushEntry(block, value));
            }
        }

        return builder.build();
    }

    @Override
    public JsonElement serialize(PushData data, Type type, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();

        data.push().forEach(pushEntry -> {
            JsonObject jsonObject = new JsonObject();
            Key blockKey = pushEntry.getBlock().key();
            jsonObject.add("block", context.serialize(blockKey, Key.class));
            jsonObject.addProperty("value", pushEntry.getValue());
            jsonObject.addProperty("ground", pushEntry.isGround());
            jsonArray.add(jsonObject);
        });

        return jsonArray;
    }
}
