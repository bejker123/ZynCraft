package com.bejker.zyn.blocks;

import com.bejker.zyn.ZynCraft;
import com.bejker.zyn.items.ZynItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ZynCraftBlocks {
    public static Block register(Block block, Identifier id,boolean shouldRegisterItem){
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    public static final Identifier ZYN_PACK_BLOCK_ID = ZynCraft.id("zyn_pack_block");
    public static final RegistryKey<Block> ZYN_PACK_BLOCK_KEY = RegistryKey.of(RegistryKeys.BLOCK,ZYN_PACK_BLOCK_ID);
    public static final ZynPackBlock ZYN_PACK_BLOCK = (ZynPackBlock) register(
            new ZynPackBlock(Block.Settings.create()
                    .registryKey(ZYN_PACK_BLOCK_KEY)),
            ZYN_PACK_BLOCK_ID,false
    );

    public static void initialize(){

    }
}
