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
 * @version 1.1.0
 * @since 1.0.0
 */
public class PushDataAdapter implements JsonDeserializer<PushData>, JsonSerializer<PushData> {

    private static final double DEFAULT_GROUND_WEIGHT = 1.0;
    private static final double DEFAULT_PUSH_WEIGHT = 0.05;

    @Override
    public PushData deserialize(JsonElement element, Type type, JsonDeserializationContext context) {
        JsonArray jsonArray = element.getAsJsonArray();
        PushData.Builder builder = PushData.builder();

        if (jsonArray.isEmpty()) {
            return builder.build(); // Return empty PushData if no entries are present
        }

        for (JsonElement jsonElement : jsonArray.asList()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Key blockKey = context.deserialize(jsonObject.get("block"), Key.class);
            int value = jsonObject.get("value").getAsInt();
            boolean ground = jsonObject.get("ground").getAsBoolean();
            double weight = jsonObject.has("weight")
                    ? jsonObject.get("weight").getAsDouble()
                    : (ground ? DEFAULT_GROUND_WEIGHT : DEFAULT_PUSH_WEIGHT);
            Block block = Block.fromKey(blockKey);

            if (ground) {
                builder.add(0, PushEntry.groundEntry(block, value, weight));
            } else {
                builder.add(PushEntry.pushEntry(block, value, weight));
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
            jsonObject.addProperty("weight", pushEntry.getWeight());
            jsonArray.add(jsonObject);
        });

        return jsonArray;
    }
}
