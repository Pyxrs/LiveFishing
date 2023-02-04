package io.github.simplycmd.fishing.data.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.simplycmd.fishing.data.FishData;
import io.github.simplycmd.fishing.data.FishSerializer;
import java.util.Objects;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class BasicFish {

    public static final Serializer SERIALIZER = new Serializer();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final ItemStack itemStack;
    private final EntityType<?> entityType;

    public BasicFish(EntityType<?> entityType, ItemStack itemStack) {
        this.entityType = entityType;
        this.itemStack = itemStack;
    }

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        String itemName = JsonHelper.getString(json, "item");

        Item item = Registries.ITEM.get(new Identifier(itemName));

        if (readNBT && json.has("tag")) {
            try {
                JsonElement element = json.get("tag");
                NbtCompound nbt;
                if (element.isJsonObject()) {
                    nbt = StringNbtReader.parse(GSON.toJson(element));
                } else {
                    nbt = StringNbtReader.parse(JsonHelper.asString(element, "tag"));
                }

                NbtCompound tmp = new NbtCompound();

                tmp.put("tag", nbt);
                tmp.putString("id", itemName);
                tmp.putInt("Count", JsonHelper.getInt(json, "count", 1));

                return ItemStack.fromNbt(tmp);
            } catch (CommandSyntaxException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e);
            }
        }

        return new ItemStack(item, JsonHelper.getInt(json, "count", 1));
    }

    public FishData toFishData() {
        return new FishData(entityType, itemStack);
    }

    public static class Serializer extends FishSerializer<BasicFish> {

        public Serializer() {
            super(new Identifier("fishing", "basic"));
        }

        @Override
        public BasicFish deserialize(JsonObject object) {

            Builder builder = Builder.create();
            builder.setStack(getItemStack(JsonHelper.getObject(object, "item"), true));
            builder.setEntityType(Registries.ENTITY_TYPE.get(Identifier.tryParse(JsonHelper.getString(object, "entity", "minecraft:armor_stand"))));

            return builder.build();
        }

        @Override
        public JsonObject serialize(BasicFish trade) {
            JsonObject object = super.serialize(trade);
            object.add("item", serializeItemStack(trade.itemStack));
            object.addProperty("entity", Registries.ENTITY_TYPE.getId(trade.entityType).toString());
            return object;
        }

        private JsonObject serializeItemStack(ItemStack stack) {
            JsonObject object = new JsonObject();
            object.addProperty("item", Objects.requireNonNull(Registries.ITEM.getId(stack.getItem())).toString());
            object.addProperty("count", stack.getCount());
            if (stack.hasNbt()) {
                object.addProperty("tag", Objects.requireNonNull(stack.getNbt()).toString());
            }
            return object;
        }
    }

    public static class Builder {

        private ItemStack itemStack;
        private EntityType<?> fish;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public BasicFish build() {
            return new BasicFish(this.fish, this.itemStack);
        }

        public Builder setStack(ItemStack stack) {
            this.itemStack = stack;
            return this;
        }

        public Builder setEntityType(EntityType<?> entityType) {
            this.fish = entityType;
            return this;
        }

    }

}
