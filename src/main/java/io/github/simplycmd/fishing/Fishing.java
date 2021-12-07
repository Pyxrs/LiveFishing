package io.github.simplycmd.fishing;

import io.github.simplycmd.fishing.data.FishManager;
import io.github.simplycmd.fishing.data.serialization.BasicFish;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;

import static io.github.simplycmd.fishing.data.FishManager.manager;

public class Fishing implements ModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(manager());
    }
}
