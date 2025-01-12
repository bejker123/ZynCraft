package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraft;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.bejker.zyn.items.ZynCraftItems.ZYN_PACK_KEY;

public class ZynPackItem extends Item {
    public static final ComponentType<Integer> ZYN_AMOUNT_COMP = Registry.register(Registries.DATA_COMPONENT_TYPE, ZynCraft.id("zyn_amount")
            ,ComponentType.<Integer>builder().codec(Codecs.POSITIVE_INT).build());

    public static final int MAX_ZYN_AMOUNT = 20;

    public static final Settings ZynPackItemSettings = new Settings()
            .registryKey(ZYN_PACK_KEY)
            .useItemPrefixedTranslationKey()
            .maxCount(1)
            .component(ZYN_AMOUNT_COMP,MAX_ZYN_AMOUNT);
    public ZynPackItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.set(ZYN_AMOUNT_COMP,MAX_ZYN_AMOUNT);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Integer amount = stack.get(ZYN_AMOUNT_COMP);
        if(amount == null){
            amount = 0;
        }
        tooltip.add(Text.literal(amount+"/"+MAX_ZYN_AMOUNT).withColor(Colors.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
        //for (var i : stack.getComponents()){
        //    tooltip.add(Text.literal(i.toString()));
        //}
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return MathHelper.clamp(Math.round((float)stack.get(ZYN_AMOUNT_COMP) * 13.0F / (float)MAX_ZYN_AMOUNT), 0, 13);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0F, ((float)stack.get(ZYN_AMOUNT_COMP)) / (float)MAX_ZYN_AMOUNT);
        return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }
}
