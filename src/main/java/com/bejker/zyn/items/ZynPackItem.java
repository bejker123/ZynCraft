package com.bejker.zyn.items;

import net.minecraft.item.Item;

import static com.bejker.zyn.items.ZynCraftItems.ZYN_KEY;
import static com.bejker.zyn.items.ZynCraftItems.ZYN_PACK_KEY;

public class ZynPackItem extends Item {
    public static final Settings ZynPackItemSettings = new Settings().registryKey(ZYN_PACK_KEY).useItemPrefixedTranslationKey().maxCount(1);
    public ZynPackItem(Settings settings) {
        super(settings);
    }


}
