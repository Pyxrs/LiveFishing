package io.github.simplycmd.fishing.data.serialization;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.simplycmd.fishing.data.FishData;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class IdkNameFish
{
    private final List<FishData> fishDataList;

    public IdkNameFish(List<FishData> fishDataList)
    {
        this.fishDataList = ImmutableList.copyOf(fishDataList);
    }

    public List<FishData> getFishDataList()
    {
        return this.fishDataList;
    }

    public static class Builder
    {
        private final List<FishData> tradeMap = Util.make(() ->
        {
            List<FishData> map = new ArrayList<>();
            return map;
        });

        private Builder() {}

        public IdkNameFish build()
        {
            return new IdkNameFish(this.tradeMap);
        }

        public void deserialize(JsonObject object)
        {
            List<FishData> fishing = this.tradeMap;

            if(JsonHelper.getBoolean(object, "replace", false))
            {
                fishing.clear();
            }

            JsonArray tradeArray = JsonHelper.getArray(object, "fishing");
            for(JsonElement tradeElement : tradeArray)
            {
                JsonObject tradeObject = tradeElement.getAsJsonObject();
                String rawType = JsonHelper.getString(tradeObject, "type");
//                Identifier typeKey = Identifier.tryParse(rawType);
//                if(typeKey == null)
//                {
//                    throw new JsonParseException("");
//                }
                fishing.add(BasicFish.SERIALIZER.deserialize(tradeObject).toFishData());
            }
        }

        public static Builder create()
        {
            return new Builder();
        }
    }
}
