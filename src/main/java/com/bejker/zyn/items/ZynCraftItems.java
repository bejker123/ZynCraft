package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ZynCraftItems {
    public static Item register(Item item, Identifier id){
        return Registry.register(Registries.ITEM, id, item);
    }

    public static final Identifier ZYN_ID = ZynCraft.id("zyn");
    public static final RegistryKey<Item> ZYN_KEY = RegistryKey.of(RegistryKeys.ITEM,ZYN_ID);

    public static final Item ZYN = register(
            new ZynItem(ZynItem.ZynItemSettings),
            ZYN_ID
    );

    public static final Identifier ZYN_PACK_ID = ZynCraft.id("zyn_pack");
    public static final RegistryKey<Item> ZYN_PACK_KEY = RegistryKey.of(RegistryKeys.ITEM,ZYN_PACK_ID);

    public static final Item ZYN_PACK = register(
            new ZynPackItem(ZynPackItem.ZynPackItemSettings),
            ZYN_PACK_ID
    );

    public static void initialize(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ZYN));
    }
}
