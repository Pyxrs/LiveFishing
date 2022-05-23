package io.github.simplycmd.fishing;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemTags {
    public static final TagKey<Item> FISHING_RODS = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "fishing_rods"));
}
