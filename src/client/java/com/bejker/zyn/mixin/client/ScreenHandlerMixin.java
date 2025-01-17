package com.bejker.zyn.mixin.client;

import com.bejker.zyn.ZynCraft;
import com.bejker.zyn.inventory.ZynInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow protected abstract Slot addSlot(Slot slot);

    @Inject(method = "addPlayerSlots",at = @At("RETURN"))
    private void init(Inventory playerInventory, int left, int top, CallbackInfo ci){
        if(playerInventory != null){
            ZynInventory inventory = (ZynInventory) playerInventory;
            Slot slot = inventory.zynCraft$getZynSlot();
            this.addSlot(slot);
        }else{
            ZynCraft.LOGGER.info("Failed to add zyn slot.");
        }
    }

@Inject(method = "internalOnSlotClick",at= @At("HEAD"))
    private void internalOnSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        //ZynCraft.LOGGER.info("{}",slotIndex);

    }
}
