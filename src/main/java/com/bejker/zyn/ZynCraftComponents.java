package com.bejker.zyn;

import com.bejker.zyn.items.ZynItem;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

public class ZynCraftComponents {
    //In ticks = 5min = 20 ticks/sec * 300sec = 6000 ticks
    public static final int MAX_ZYN_DURABILITY = 6_000;
    public static final int MAX_ZYN_COUNT = 1;
    public static final ComponentType<ZynItem.ZynType> ZYN_TYPE = Registry.register(Registries.DATA_COMPONENT_TYPE,ZynCraft.id("zyn_type"),
            ComponentType.<ZynItem.ZynType>builder().codec(new Codec<ZynItem.ZynType>() {
                @Override
                public <T> DataResult<T> encode(ZynItem.ZynType zynType, DynamicOps<T> dynamicOps, T t) {
                    dynamicOps.createInt(zynType.ordinal());
                    return DataResult.success(t);
                }

                @Override
                public <T> DataResult<Pair<ZynItem.ZynType, T>> decode(DynamicOps<T> dynamicOps, T t) {
                    try {
                        int i = ((NbtInt) t).intValue();
                        return DataResult.success(Pair.of(ZynItem.ZynType.from(i), t));
                    } catch (Exception e) {
                        return DataResult.error(() -> "Failed to decode zyn type.");
                    }
                }
            }).build());
    public static final AttachmentType<Integer> NICOTINE_CONTENT = AttachmentRegistry.<Integer>create(
            ZynCraft.id("nicotine_content"),
            builder -> builder
                    .initializer(() -> 0)
                    .persistent(Codec.INT) // persist across restarts
                    .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );
    public static final ComponentType<Integer> ZYN_AMOUNT_COMP = Registry.register(Registries.DATA_COMPONENT_TYPE, ZynCraft.id("zyn_amount")
            ,ComponentType.<Integer>builder().codec(Codecs.NON_NEGATIVE_INT).build());
    public static final int MAX_ZYN_AMOUNT = 20;
}
