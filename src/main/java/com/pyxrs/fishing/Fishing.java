package com.pyxrs.fishing;

import java.util.HashMap;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class Fishing implements ModInitializer {
    public static final HashMap<Item, EntityType<?>> FISH = new HashMap<Item, EntityType<?>>();
    static {
        FISH.put(Items.COD, EntityType.COD);
        FISH.put(Items.SALMON, EntityType.SALMON);
        FISH.put(Items.TROPICAL_FISH, EntityType.TROPICAL_FISH);
        FISH.put(Items.PUFFERFISH, EntityType.PUFFERFISH);
    }
    
    @Override
    public void onInitialize() {
    }
}
