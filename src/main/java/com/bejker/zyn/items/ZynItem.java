package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraftComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bejker.zyn.items.ZynCraftItems.ZYN;
import static com.bejker.zyn.items.ZynCraftItems.ZYN_KEY;

public class ZynItem extends Item {

    public static final int ZYN_SLOT = 45;
    public static Set<Item> ZynabbleItems = new HashSet<>();


    public static boolean canBePlacedInZynSlot(ItemStack stack){
        return ZynabbleItems.contains(stack.getItem());
    }

    public static void reset_nicotine(ServerCommandSource source, ServerPlayerEntity player){
        set_nicotine(source,player,0);
    }

    public static int get_nicotine(ServerPlayerEntity player){
        return player.getAttached(ZynCraftComponents.NICOTINE_CONTENT);
    }

    public static void set_nicotine(ServerCommandSource source, ServerPlayerEntity player, int value) {
        player.setAttached(ZynCraftComponents.NICOTINE_CONTENT,value);
        source.sendFeedback(() -> Text.literal("Set nicotine for ")
        .append(
        Text.literal(player.getName().getLiteralString()).formatted(Formatting.AQUA))
                .append(Text.literal(" to "))
                .append(Text.of(String.valueOf(value)).copy().formatted(Formatting.GOLD)), false);

    }

    public static final Item.Settings ZynItemSettings = new Item.Settings()
            .registryKey(ZYN_KEY)
            .useItemPrefixedTranslationKey()
            .maxCount(ZynCraftComponents.MAX_ZYN_COUNT)
            .maxDamage(ZynCraftComponents.MAX_ZYN_DURABILITY)
            .component(ZynCraftComponents.ZYN_STRENGTH,10)
            .component(ZynCraftComponents.ZYN_TYPE, ZynCraftComponents.ZynType.CITRUS)
            .rarity(Rarity.RARE);
    public ZynItem(Settings settings) {
        super(settings);
    }

    public ItemStack itemStackFrom(int zynStrength, ZynCraftComponents.ZynType type){
        ItemStack stack =  super.getDefaultStack();
        stack.set(ZynCraftComponents.ZYN_STRENGTH,zynStrength);
        stack.set(ZynCraftComponents.ZYN_TYPE,type);
        return stack;
    }
    @Override
    public ItemStack getDefaultStack() {
        return ZYN.itemStackFrom(10, ZynCraftComponents.ZynType.CITRUS);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(slot != ZYN_SLOT){
            return;
        }
        Integer strength = stack.get(ZynCraftComponents.ZYN_STRENGTH);
        if(strength == null){
            return;
        }
        int durability = stack.getDamage();
        if(durability == ZynCraftComponents.MAX_ZYN_DURABILITY){
            stack.setCount(0);
            return;
        }
        //if(world.getTime() % 1 == 0){
        if(entity instanceof PlayerEntity player){
            //player.setAngles(world.random.nextFloat() + player.getYaw(),world.random.nextFloat() + player.getPitch());
            stack.damage(1,player);
            Integer attached = entity.getAttached(ZynCraftComponents.NICOTINE_CONTENT);
            int amount = strength;
            if(attached != null&&attached != 0){
                amount += attached;
            }
            entity.setAttached(ZynCraftComponents.NICOTINE_CONTENT,amount);
            //player.addStatusEffect(StatusEffectInstance.)
        }
        //}
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(stack.getOrDefault(ZynCraftComponents.ZYN_TYPE, ZynCraftComponents.ZynType.CITRUS).toString()).withColor(Colors.ALTERNATE_WHITE));
        tooltip.add(Text.literal(stack.getOrDefault(ZynCraftComponents.ZYN_STRENGTH,0).toString()).withColor(Colors.CYAN)
                .append(Text.literal("mg").withColor(Colors.YELLOW)));
    }
}
