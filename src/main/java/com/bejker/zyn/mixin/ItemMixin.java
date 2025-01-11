package com.bejker.zyn.mixin;

import com.bejker.zyn.ZynCraft;
import com.bejker.zyn.inventory.ZynInventory;
import com.bejker.zyn.network.SyncInventoryPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use",at=@At("HEAD"),cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(user instanceof ServerPlayerEntity player){
            ItemStack stack = user.getStackInHand(hand);
            if(!ZynCraft.canBePlacedInZynSlot(stack)){
                return;
            }
            ZynInventory inventory = ((ZynInventory) player.getInventory());
            if(!inventory.zynCraft$getZynSlot().getStack().isEmpty()){
                cir.setReturnValue(ActionResult.FAIL);
                return;
            }
            var stack_cp = stack.copy();
            stack_cp.setCount(1);
            stack.decrement(1);
            cir.setReturnValue(ActionResult.SUCCESS);
            player.getInventory().markDirty();
            inventory.zynCraft$getZynSlot().insertStack(stack_cp,1);
            ServerPlayNetworking.send(player, new SyncInventoryPacket(player.getId(), Map.of(ZynCraft.ZYN_SLOT,stack_cp),Map.of()));
        }

    }
}
