package io.github.simplycmd.fishing;

import static io.github.simplycmd.fishing.data.FishManager.manager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class Fishing implements ModInitializer {

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(manager());
    }

}
