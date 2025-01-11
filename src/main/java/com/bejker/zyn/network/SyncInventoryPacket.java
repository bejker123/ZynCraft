package com.bejker.zyn.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.Map;
import java.util.HashMap;

public record SyncInventoryPacket(int entityId, Map<Integer, ItemStack> contentUpdates, Map<Integer, NbtCompound> inventoryUpdates) implements CustomPayload {
    public static final PacketCodec<RegistryByteBuf, SyncInventoryPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            SyncInventoryPacket::entityId,
            PacketCodecs.map(HashMap::new, PacketCodecs.INTEGER, ItemStack.OPTIONAL_PACKET_CODEC),
            SyncInventoryPacket::contentUpdates,
            PacketCodecs.map(HashMap::new, PacketCodecs.INTEGER, PacketCodecs.NBT_COMPOUND),
            SyncInventoryPacket::inventoryUpdates,
            SyncInventoryPacket::new);
    @Override
    public Id<SyncInventoryPacket> getId() {
        return ZynCraftPackets.SYNC_INVENTORY;
    }
}
