package com.bejker.zyn.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public interface ZynInventory {

    int SLOT_X = -344;
    int SLOT_Y =-203;

    DefaultedList<ItemStack> zynCraft$getZyn();

    Slot zynCraft$getZynSlot();
}
