package com.bejker.zyn.mixin.client;

import com.bejker.zyn.ZynCraftClient;
import com.bejker.zyn.ZynScreenHandler;
import com.bejker.zyn.inventory.ZynInventory;
import com.bejker.zyn.inventory.ZynSlot;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bejker.zyn.ZynCraft.ZYN_SLOT;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow protected int backgroundHeight;

    @Shadow protected int backgroundWidth;

    @Shadow protected abstract void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType);

    @Inject(method = "render", at = @At("HEAD"))
    void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        int x = ((HandledScreen)((Object) this)).x;
        int y = ((HandledScreen)((Object) this)).y;
        ZynScreenHandler.render(context,x,y,this.backgroundWidth,this.backgroundHeight,mouseX,mouseY,delta);

    }
    @Unique
    private boolean isInBounds(double mouseX, double mouseY){
        int x = this.backgroundWidth + ZynInventory.SLOT_X + 304;
        int y = this.backgroundHeight + ZynInventory.SLOT_Y + 103;

        //ZynCraft.LOGGER.info("mouseX - x = {}; moueX - x + 16 = {},mouseY - y = {}, mouseY - y + 16 = {}",
        //        mouseX - x,
        //        mouseX - x + 16,
        //        mouseY - y,
        //        mouseY - y + 16);
        return mouseX >= x&& mouseX <= x + 16 &&mouseY >= y&&mouseY <= y + 16;
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",at = @At("HEAD"), cancellable = true)
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if(!(slot instanceof ZynSlot) ||actionType != SlotActionType.THROW){
            return;
        }
        ci.cancel();
        onMouseClick(slot,ZYN_SLOT,button,SlotActionType.PICKUP);

    }

    @Inject(method = "isClickOutsideBounds",at= @At("RETURN"), cancellable = true)
    protected void isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> cir) {
        if(!this.getClass().toString().equals("class net.minecraft.client.gui.screen.ingame.InventoryScreen")){
            return;
        }
       if(isInBounds(mouseX,mouseY)){
           //ZynCraftClient.LOGGER.info("{},{}",mouseX,mouseY);
           cir.setReturnValue(false);
       }
    }

    @Inject(method = "getSlotAt", at = @At("HEAD"), cancellable = true)
    void getSlotAt(double mouseX, double mouseY, CallbackInfoReturnable<Slot> cir){
        if(!this.getClass().toString().equals("class net.minecraft.client.gui.screen.ingame.InventoryScreen")){
            return;
        }
        if(isInBounds(mouseX,mouseY)){
            cir.setReturnValue(ZynCraftClient.getZynSlot());
            cir.cancel();
        }
    }
    @Inject(method = "mouseClicked",at=@At("RETURN"))
    void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        if(!this.getClass().toString().equals("class net.minecraft.client.gui.screen.ingame.InventoryScreen")){
            return;
        }
        //ZynCraftClient.LOGGER.info("{},{}, Button: {}, Ret: {}",mouseX,mouseY,button,cir.getReturnValue());
    }

}
