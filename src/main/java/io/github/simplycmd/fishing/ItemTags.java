package io.github.simplycmd.fishing;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemTags {
    public static final Tag<Item> FISHING_RODS = TagFactory.ITEM.create(new Identifier("c", "fishing_rods"));
}
