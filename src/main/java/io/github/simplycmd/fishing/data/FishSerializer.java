package io.github.simplycmd.fishing.data;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

public abstract class FishSerializer<T> {

    private final Identifier id;

    public FishSerializer(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return this.id;
    }

    public abstract T deserialize(JsonObject object);

    public JsonObject serialize(T trade) {
        JsonObject object = new JsonObject();
        object.addProperty("type", this.id.toString());
        return object;
    }

}
