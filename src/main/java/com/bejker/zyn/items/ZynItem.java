package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.bejker.zyn.items.ZynCraftItems.ZYN_KEY;

public class ZynItem extends Item {
    public static final Item.Settings ZynItemSettings = new Item.Settings().registryKey(ZYN_KEY).useItemPrefixedTranslationKey();
    public ZynItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(slot != ZynCraft.ZYN_SLOT){
            return;
        }
        if(world.getTime() % 1 == 0){
            if(entity instanceof PlayerEntity player){
                player.setAngles(world.random.nextFloat() + player.getYaw(),world.random.nextFloat() + player.getPitch());
            }
        }
    }
}
