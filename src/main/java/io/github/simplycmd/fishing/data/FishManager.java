package io.github.simplycmd.fishing.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.simplycmd.fishing.data.serialization.NamedFish;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

public class FishManager implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    public static FishManager manager;
    public static List<NamedFish> tradeMap = new ArrayList<>();

    public static FishManager manager() {
        if (manager == null) {
            manager = new FishManager();
        }
        return manager;
    }

    @Nullable
    public FishData getFish(ItemStack itemStack) {
        for (var data : tradeMap) {
            for (var data2 : data.getFishDataList()) {
                if (data2.itemStack().equals(itemStack)) {
                    return data2;
                }
            }
        }
        return null;
    }

    @Nullable
    public FishData getFish(Item item) {
        for (var data : tradeMap) {
            for (var data2 : data.getFishDataList()) {
                if (data2.itemStack().getItem().equals(item)) {
                    return data2;
                }
            }
        }
        return null;
    }

    /**
     * @return The unique identifier of this listener.
     */
    @Override
    public Identifier getFabricId() {
        return new Identifier("fishing", "fish_loader");
    }

    @Override
    public void reload(ResourceManager manager) {
        List<NamedFish> entityToResourceList = new ArrayList<>();
        // Clear Caches Here

        for (Identifier id : manager.findResources("fishing", path -> path.getPath().equals("fishing/fish.json")).keySet()) {
            manager.getResource(id).ifPresent(resource -> {
                try (InputStream stream = resource.getInputStream()) {
                    NamedFish.Builder builder = NamedFish.Builder.create();
                    Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                    JsonObject object = JsonHelper.deserialize(GSON, reader, JsonObject.class);
                    builder.deserialize(object);
                    entityToResourceList.add(builder.build());
                    tradeMap = ImmutableList.copyOf(entityToResourceList);
                    // Consume the stream however you want, medium, rare, or well done.
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
