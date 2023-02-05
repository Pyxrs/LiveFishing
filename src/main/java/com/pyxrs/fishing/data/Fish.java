package com.pyxrs.fishing.data;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record Fish(@NotNull EntityType<?> fish, ItemStack itemStack) {
}