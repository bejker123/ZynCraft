package com.bejker.zyn.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ZynSlot extends Slot {
    public ZynSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void onQuickTransfer(ItemStack newItem, ItemStack original) {
        int i = original.getCount() - newItem.getCount();
        if (i > 0) {
            this.onCrafted(original, i);
        }

    }

    @Override
    protected void onCrafted(ItemStack stack, int amount) {
    }

    @Override
    protected void onTake(int amount) {
    }

    @Override
    protected void onCrafted(ItemStack stack) {
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.markDirty();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getStack() {
        return this.inventory.getStack(this.getIndex());
    }

    @Override
    public boolean hasStack() {
        return !this.getStack().isEmpty();
    }

    @Override
    public void setStack(ItemStack stack) {
        this.setStack(stack, this.getStack());
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        this.setStackNoCallbacks(stack);
    }

    @Override
    public void setStackNoCallbacks(ItemStack stack) {
        this.inventory.setStack(this.getIndex(), stack);
        this.markDirty();
    }

    @Override
    public void markDirty() {
        this.inventory.markDirty();
    }

    @Override
    public int getMaxItemCount() {
        return this.inventory.getMaxCountPerStack();
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return Math.min(this.getMaxItemCount(), stack.getMaxCount());
    }

    @Nullable
    @Override
    public Identifier getBackgroundSprite() {
        return null;
    }

    @Override
    public ItemStack takeStack(int amount) {
        return this.inventory.removeStack(this.getIndex(), amount);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Optional<ItemStack> tryTakeStackRange(int min, int max, PlayerEntity player) {
        if (!this.canTakeItems(player)) {
            return Optional.empty();
        } else if (!this.canTakePartial(player) && max < this.getStack().getCount()) {
            return Optional.empty();
        } else {
            min = Math.min(min, max);
            ItemStack itemStack = this.takeStack(min);
            if (itemStack.isEmpty()) {
                return Optional.empty();
            } else {
                if (this.getStack().isEmpty()) {
                    this.setStack(ItemStack.EMPTY, itemStack);
                }

                return Optional.of(itemStack);
            }
        }
    }

    @Override
    public ItemStack takeStackRange(int min, int max, PlayerEntity player) {
        Optional<ItemStack> optional = this.tryTakeStackRange(64, 64, player);
        optional.ifPresent((stack) -> {
            this.onTakeItem(player, stack);
        });
        return (ItemStack)optional.orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack insertStack(ItemStack stack) {
        return this.insertStack(stack, stack.getCount());
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        if (!stack.isEmpty() && this.canInsert(stack)) {
            ItemStack itemStack = this.getStack();
            int i = Math.min(Math.min(count, stack.getCount()), this.getMaxItemCount(stack) - itemStack.getCount());
            if (i <= 0) {
                return stack;
            } else {
                if (itemStack.isEmpty()) {
                    this.setStack(stack.split(i));
                } else if (ItemStack.areItemsAndComponentsEqual(itemStack, stack)) {
                    stack.decrement(i);
                    itemStack.increment(i);
                    this.setStack(itemStack);
                }

                return stack;
            }
        } else {
            return stack;
        }
    }

    @Override
    public boolean canTakePartial(PlayerEntity player) {
        return this.canTakeItems(player) && this.canInsert(this.getStack());
    }

    @Override
    public boolean canBeHighlighted() {
        return true;
    }

    @Override
    public boolean disablesDynamicDisplay() {
        return false;
    }

}
