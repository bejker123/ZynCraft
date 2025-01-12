package com.bejker.zyn.items;

import com.bejker.zyn.ZynCraft;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Rarity;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;

import static com.bejker.zyn.items.ZynCraftItems.ZYN;
import static com.bejker.zyn.items.ZynCraftItems.ZYN_KEY;

public class ZynItem extends Item {

    public enum ZynType{
        CITRUS;

        public static ZynType from(int value) {
            return ZynType.CITRUS;
        }
    }

    //In ticks = 5min = 20 ticks/sec * 300sec = 6000 ticks
    public static final int MAX_ZYN_DURABILITY = 6_000;
    public static final int MAX_ZYN_COUNT = 1;

    public static final ComponentType<ZynType> ZYN_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE,ZynCraft.id("zyn_type"),
            ComponentType.<ZynType>builder().codec(new Codec<ZynType>() {
                @Override
                public <T> DataResult<T> encode(ZynType zynType, DynamicOps<T> dynamicOps, T t) {
                    dynamicOps.createInt(zynType.ordinal());
                    return DataResult.success(t);
                }

                @Override
                public <T> DataResult<Pair<ZynType, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    try {
                        int i = ((NbtInt) t).intValue();
                        return DataResult.success(Pair.of(ZynType.from(i), t));
                    } catch (Exception e) {
                        return DataResult.error(() -> "Failed to decode zyn type.");
                    }
                }
            }).build());

    //In mg
    public static final ComponentType<Integer> ZYN_STRENGTH = Registry.register(Registries.DATA_COMPONENT_TYPE, ZynCraft.id("zyn_strength")
            ,ComponentType.<Integer>builder().codec(Codecs.POSITIVE_INT).build());
    public static final Item.Settings ZynItemSettings = new Item.Settings()
            .registryKey(ZYN_KEY)
            .useItemPrefixedTranslationKey()
            .maxCount(MAX_ZYN_COUNT)
            .maxDamage(MAX_ZYN_DURABILITY)
            .component(ZYN_STRENGTH,10)
            .component(ZYN_TYPE,ZynType.CITRUS)
            .rarity(Rarity.RARE);
    public ZynItem(Settings settings) {
        super(settings);
    }

    public ItemStack itemStackFrom(int zynStrength,ZynType type){
        ItemStack stack =  super.getDefaultStack();
        stack.set(ZYN_STRENGTH,zynStrength);
        stack.set(ZYN_TYPE,type);
        return stack;
    }
    @Override
    public ItemStack getDefaultStack() {
        return ZYN.itemStackFrom(10,ZynType.CITRUS);
    }

    public static final AttachmentType<Integer> NICOTINE_CONTENT = AttachmentRegistry.<Integer>create(
            ZynCraft.id("nicotine_content"),
            builder -> builder
                    .initializer(() -> 0)
                    .persistent(Codec.INT) // persist across restarts
                    .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(slot != ZynCraft.ZYN_SLOT){
            return;
        }
        Integer strength = stack.get(ZYN_STRENGTH);
        if(strength == null){
            return;
        }
        int durability = stack.getDamage();
        if(durability == MAX_ZYN_DURABILITY){
            stack.setCount(0);
            return;
        }
        //if(world.getTime() % 1 == 0){
        if(entity instanceof PlayerEntity player){
            //player.setAngles(world.random.nextFloat() + player.getYaw(),world.random.nextFloat() + player.getPitch());
            stack.damage(1,player);
            Integer attached = entity.getAttached(NICOTINE_CONTENT);
            int amount = 1;
            if(attached != null&&attached != 0){
                amount += attached;
            }
            entity.setAttached(NICOTINE_CONTENT,amount);
            //player.addStatusEffect(StatusEffectInstance.)
        }
        //}
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(stack.getOrDefault(ZYN_TYPE,ZynType.CITRUS).toString()).withColor(Colors.ALTERNATE_WHITE));
        tooltip.add(Text.literal(stack.getOrDefault(ZYN_STRENGTH,0).toString()).withColor(Colors.CYAN)
                .append(Text.literal("mg").withColor(Colors.YELLOW)));
    }
}
