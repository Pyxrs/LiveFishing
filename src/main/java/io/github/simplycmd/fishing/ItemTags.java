package io.github.simplycmd.fishing;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ItemTags {

    public static final TagKey<Item> FISHING_RODS = TagKey.of(Registries.ITEM.getKey(), new Identifier("c", "fishing_rods"));

}
