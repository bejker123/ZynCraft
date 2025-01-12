package com.bejker.zyn.items;

import net.minecraft.item.Item;

import static com.bejker.zyn.items.ZynCraftItems.ZYN_KEY;

public class ZynItem extends Item {
    public static final Item.Settings ZynItemSettings = new Item.Settings().registryKey(ZYN_KEY).useItemPrefixedTranslationKey();
    public ZynItem(Settings settings) {
        super(settings);
    }
}
