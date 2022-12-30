package io.github.simplycmd.fishing.data;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record FishData(@NotNull EntityType<?> fish, ItemStack itemStack) {

}
