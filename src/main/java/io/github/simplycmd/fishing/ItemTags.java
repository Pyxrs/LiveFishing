package io.github.simplycmd.fishing;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ItemTags {
    public static final Tag<Item> FISHING_RODS = TagFactory.ITEM.create(new Identifier("c", "fishing_rods"));
}
