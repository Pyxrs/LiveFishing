package com.pyxrs.fishing.data.serialization;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pyxrs.fishing.Fishing;
import com.pyxrs.fishing.data.Fish;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

public class NamedFish {

    private final List<Fish> fishDataList;

    public NamedFish(List<Fish> fishDataList) {
        this.fishDataList = ImmutableList.copyOf(fishDataList);
    }

    public List<Fish> getFishDataList() {
        return this.fishDataList;
    }

    public static class Builder {

        private final List<Fish> tradeMap = Util.make(() -> (List<Fish>) new ArrayList<Fish>());

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public NamedFish build() {
            return new NamedFish(this.tradeMap);
        }

        public void deserialize(JsonObject object) {
            List<Fish> fishing = this.tradeMap;

            if (JsonHelper.getBoolean(object, "replace", false)) {
                fishing.clear();
            }

            JsonArray tradeArray = JsonHelper.getArray(object, Fishing.MOD_ID);
            for (JsonElement tradeElement : tradeArray) {
                JsonObject tradeObject = tradeElement.getAsJsonObject();
                fishing.add(BasicFish.SERIALIZER.deserialize(tradeObject).toFishData());
            }
        }

    }

}
