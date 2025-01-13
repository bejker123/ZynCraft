package com.bejker.zyn.mixin;

import com.bejker.zyn.inventory.ZynInventory;
import com.bejker.zyn.inventory.ZynSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bejker.zyn.items.ZynItem.ZYN_SLOT;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements ZynInventory {
    @Shadow @Final public PlayerEntity player;

    @Shadow @Final public DefaultedList<ItemStack> main;
    @Shadow @Final public DefaultedList<ItemStack> armor;
    @Shadow @Final public DefaultedList<ItemStack> offHand;
    @Unique
    private static final int BYTE_OFFSET = 200;
    @Unique
    public DefaultedList<ItemStack> zyn;
    @Unique
    public Slot zyn_slot;
    @Shadow
    public int selectedSlot;

    @Inject(method = "<init>", at = @At("TAIL"))
    void init(PlayerEntity player, CallbackInfo ci){
        zyn_slot = new ZynSlot((Inventory) this, ZYN_SLOT,-18,7);
        zyn_slot.id = ZYN_SLOT;
        zyn = DefaultedList.ofSize(1,ItemStack.EMPTY);
    }

    @Inject(method = "updateItems", at = @At("TAIL"))
    public void updateItems(CallbackInfo ci) {
        zyn.getFirst().inventoryTick(this.player.getWorld(), this.player, zyn_slot.id, this.selectedSlot == zyn_slot.id);
    }
    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", cancellable = true,at=@At("HEAD"))
    void removeStack(int slot, CallbackInfoReturnable<ItemStack> cir){
        if(slot != ZYN_SLOT){
            return;
        }
        if(zyn.getFirst() == ItemStack.EMPTY||zyn.getFirst() == null){
            cir.setReturnValue(ItemStack.EMPTY);
            cir.cancel();
            return;
        }
        cir.setReturnValue(zyn.removeFirst());
    }
    @Inject(method = "removeStack(II)Lnet/minecraft/item/ItemStack;",at= @At("HEAD"),cancellable = true)
    public void removeStack(int slot, int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (slot != ZYN_SLOT){
            return;
        }
        cir.setReturnValue(zyn != null && !((ItemStack)zyn.getFirst()).isEmpty() ? Inventories.splitStack(zyn, 0, amount) : ItemStack.EMPTY);
        cir.cancel();
    }


    @Inject(method = "removeOne",at = @At("HEAD"))
    public void removeOne(ItemStack stack, CallbackInfo ci) {
        if (zyn.getFirst() == stack) {
            zyn.set(0, ItemStack.EMPTY);
        }
    }

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    void insertStack(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(slot != ZYN_SLOT||stack.isEmpty()){
            return;
        }
        if(zyn.getFirst() != ItemStack.EMPTY){
            return;
        }
        zyn.set(0,stack);
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Inject(method = "setStack",at=@At("HEAD"), cancellable = true)
     void setStack(int slot, ItemStack stack, CallbackInfo ci){
        if(slot != ZYN_SLOT||stack.isEmpty()){
            return;
        }
        zyn.set(0,stack);
        ci.cancel();
    }

    @Inject(method = "getStack", at = @At("HEAD"), cancellable = true)
    void getStack(int slot, CallbackInfoReturnable<ItemStack> cir){
        if (slot == ZYN_SLOT){
            ItemStack stack = zyn.getFirst();
            cir.setReturnValue(stack);
            cir.cancel();
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    void writeNbt(NbtList nbtList, CallbackInfoReturnable<NbtList> cir){
        NbtCompound nbtCompound;
        for(int i = 0; i < this.zyn.size(); ++i) {
            if (!this.zyn.get(i).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)(i + BYTE_OFFSET));
                nbtList.add(this.zyn.get(i).toNbt(this.player.getRegistryManager(), nbtCompound));
            }
        }

       /// nbtList.add(((ItemStack)this.offHand.get(i)).toNbt(this.player.getRegistryManager(), nbtCompound));
    }
    @Inject(method = "readNbt",at=@At("HEAD"), cancellable = true)
    void readNbt(NbtList nbtList, CallbackInfo ci){
        ci.cancel();
        this.main.clear();
        this.armor.clear();
        this.offHand.clear();
        this.zyn.clear();

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.fromNbt(this.player.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY);
            if (j >= 0 && j < this.main.size()) {
                this.main.set(j, itemStack);
            } else if (j >= 100 && j < this.armor.size() + 100) {
                this.armor.set(j - 100, itemStack);
            } else if (j >= 150 && j < this.offHand.size() + 150) {
                this.offHand.set(j - 150, itemStack);
            }else if(j >= BYTE_OFFSET && j < this.zyn.size() + BYTE_OFFSET){
                this.zyn.set(j - BYTE_OFFSET, itemStack);
            }
        }


    }

    @Override
    public DefaultedList<ItemStack> zynCraft$getZyn() {
        return zyn;
    }
    @Override
    public Slot zynCraft$getZynSlot(){
       return zyn_slot;
    }

}
