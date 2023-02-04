package io.github.simplycmd.fishing.data.serialization;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.simplycmd.fishing.data.FishData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

public class NamedFish {

    private final List<FishData> fishDataList;

    public NamedFish(List<FishData> fishDataList) {
        this.fishDataList = ImmutableList.copyOf(fishDataList);
    }

    public List<FishData> getFishDataList() {
        return this.fishDataList;
    }

    public static class Builder {

        private final List<FishData> tradeMap = Util.make(() -> (List<FishData>) new ArrayList<FishData>());

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public NamedFish build() {
            return new NamedFish(this.tradeMap);
        }

        public void deserialize(JsonObject object) {
            List<FishData> fishing = this.tradeMap;

            if (JsonHelper.getBoolean(object, "replace", false)) {
                fishing.clear();
            }

            JsonArray tradeArray = JsonHelper.getArray(object, "fishing");
            for (JsonElement tradeElement : tradeArray) {
                JsonObject tradeObject = tradeElement.getAsJsonObject();
                fishing.add(BasicFish.SERIALIZER.deserialize(tradeObject).toFishData());
            }
        }

    }

}
