package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraftComponents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Objects;

import static com.bejker.zyn.items.ZynCraftItems.ZYN;
import static com.bejker.zyn.items.ZynCraftItems.ZYN_PACK_KEY;
import static com.bejker.zyn.ZynCraftComponents.ZYN_STRENGTH;
import static com.bejker.zyn.ZynCraftComponents.ZYN_TYPE;

public class ZynPackItem extends Item {
    public static final Settings ZynPackItemSettings = new Settings()
            .registryKey(ZYN_PACK_KEY)
            .useItemPrefixedTranslationKey()
            .maxCount(1)
            .component(ZynCraftComponents.ZYN_AMOUNT_COMP, ZynCraftComponents.MAX_ZYN_AMOUNT)
            .component(ZYN_STRENGTH,10)
            .component(ZYN_TYPE, ZynCraftComponents.ZynType.CITRUS)
            .rarity(Rarity.EPIC);
    public ZynPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.set(ZynCraftComponents.ZYN_AMOUNT_COMP, ZynCraftComponents.MAX_ZYN_AMOUNT);
        stack.set(ZYN_STRENGTH,10);
        stack.set(ZYN_TYPE, ZynCraftComponents.ZynType.CITRUS);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Integer amount = stack.get(ZynCraftComponents.ZYN_AMOUNT_COMP);
        if(amount == null){
            amount = 0;
        }
        ZYN.appendTooltip(stack,context,tooltip,type);
        int color = this.getItemBarColor(stack);
        tooltip.add(Text.literal(amount.toString()).withColor(color)
                .append(Text.literal("/").copy().withColor(Colors.ALTERNATE_WHITE))
                .append(Text.literal(String.valueOf(ZynCraftComponents.MAX_ZYN_AMOUNT))
                        .withColor(Colors.GREEN)));
        //tooltip.add(
        //        Text.literal(amount+"/"+ ZynCraftComponents.MAX_ZYN_AMOUNT).withColor(this.getItemBarColor(stack)));
        super.appendTooltip(stack, context, tooltip, type);
        //for (var i : stack.getComponents()){
        //    tooltip.add(Text.literal(i.toString()));
        //}
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round((float)stack.get(ZynCraftComponents.ZYN_AMOUNT_COMP) * 13.0F / (float) ZynCraftComponents.MAX_ZYN_AMOUNT), 0, 13);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0F, ((float)stack.get(ZynCraftComponents.ZYN_AMOUNT_COMP)) / (float) ZynCraftComponents.MAX_ZYN_AMOUNT);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public static ItemStack PackItemStackToZynItemStack(ItemStack stack){

        if(stack.getItem() != ZynCraftItems.ZYN_PACK){
           return ItemStack.EMPTY;
        }
        return ZYN.itemStackFrom(stack.getOrDefault(ZYN_STRENGTH,10),stack.get(ZYN_TYPE));
    }
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity player){
            ItemStack stack = user.getStackInHand(hand);
            Integer zyn_amount = stack.get(ZynCraftComponents.ZYN_AMOUNT_COMP);
            if(zyn_amount == null||zyn_amount <= 0){
                return super.use(world, user, hand);
            }
            ItemStack zyn_stack = PackItemStackToZynItemStack(stack);
            if(zyn_stack == ItemStack.EMPTY){
                return super.use(world, user, hand);
            }
            int count = user.isSneaking()? Math.min(zyn_amount, ZynCraftComponents.MAX_ZYN_AMOUNT) : 1;
            zyn_stack.setCount(count);
            stack.set(ZynCraftComponents.ZYN_AMOUNT_COMP,zyn_amount - count);
            player.giveOrDropStack(zyn_stack);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public static boolean isTheSameZyn(ItemStack stack,ItemStack otherStack){
        return stack.get(ZYN_TYPE) == otherStack.get(ZYN_TYPE)
                && Objects.equals(stack.get(ZYN_STRENGTH), otherStack.get(ZYN_STRENGTH));
    }
    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
            Integer zyn_amount = stack.get(ZynCraftComponents.ZYN_AMOUNT_COMP);
            if (zyn_amount == null||otherStack.isDamaged()) {
                return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
            }
            if(!ZynItem.canBePlacedInZynSlot(otherStack)){
                if(otherStack.getItem() == Items.AIR &&clickType == ClickType.RIGHT&&zyn_amount > 0){
                    ItemStack cursor_stack = PackItemStackToZynItemStack(stack);
                    cursor_stack.setCount(1);
                    cursorStackReference.set(cursor_stack);
                    stack.set(ZynCraftComponents.ZYN_AMOUNT_COMP,zyn_amount - 1);
                    return true;
                }
                return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
            }
            if (zyn_amount == ZynCraftComponents.MAX_ZYN_AMOUNT||!isTheSameZyn(stack,otherStack)) {
                return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
            }
            if(clickType == ClickType.LEFT){
               int count = Math.min(zyn_amount + otherStack.getCount(), ZynCraftComponents.MAX_ZYN_AMOUNT);
               otherStack.decrement(count - zyn_amount);
               stack.set(ZynCraftComponents.ZYN_AMOUNT_COMP,count);
            }else if(clickType == ClickType.RIGHT){
                otherStack.decrement(1);
                stack.set(ZynCraftComponents.ZYN_AMOUNT_COMP,zyn_amount + 1);
            }
            return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult actionResult = this.place(new ItemPlacementContext(context));
        return !actionResult.isAccepted() && context.getStack().contains(DataComponentTypes.CONSUMABLE) ? super.use(context.getWorld(), context.getPlayer(), context.getHand()) : actionResult;
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
    }
    public ActionResult place(ItemPlacementContext context) {
        //if (!this.getBlock().isEnabled(context.getWorld().getEnabledFeatures())) {
        //    return ActionResult.FAIL;
        //} else if (!context.canPlace()) {
        //    return ActionResult.FAIL;
        //} else {
        //    ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
        //    if (itemPlacementContext == null) {
        //        return ActionResult.FAIL;
        //    } else {
        //        BlockState blockState = this.getPlacementState(itemPlacementContext);
        //        if (blockState == null) {
        //            return ActionResult.FAIL;
        //        } else if (!this.place(itemPlacementContext, blockState)) {
        //            return ActionResult.FAIL;
        //        } else {
        //            BlockPos blockPos = itemPlacementContext.getBlockPos();
        //            World world = itemPlacementContext.getWorld();
        //            PlayerEntity playerEntity = itemPlacementContext.getPlayer();
        //            ItemStack itemStack = itemPlacementContext.getStack();
        //            BlockState blockState2 = world.getBlockState(blockPos);
        //            if (blockState2.isOf(blockState.getBlock())) {
        //                blockState2 = this.placeFromNbt(blockPos, world, itemStack, blockState2);
        //                this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
        //                copyComponentsToBlockEntity(world, blockPos, itemStack);
        //                blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
        //                if (playerEntity instanceof ServerPlayerEntity) {
        //                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
        //                }
        //            }

        //            BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
        //            world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
        //            world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(playerEntity, blockState2));
        //            itemStack.decrementUnlessCreative(1, playerEntity);
        //            return ActionResult.SUCCESS;
        //        }
        //    }
        //}
        return ActionResult.PASS;
    }

    private Block getBlock() {
        return Blocks.DIRT;
    }
}
