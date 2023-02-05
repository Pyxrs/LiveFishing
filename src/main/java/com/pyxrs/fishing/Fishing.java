package com.pyxrs.fishing;

import static com.pyxrs.fishing.data.FishManager.manager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class Fishing implements ModInitializer {
    public static final String MOD_ID = "fishing";

    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(manager());
    }
}
