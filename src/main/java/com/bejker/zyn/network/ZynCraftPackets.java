package com.bejker.zyn.network;

import com.bejker.zyn.ZynCraft;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ZynCraftPackets {
    public static final CustomPayload.Id<SyncInventoryPacket> SYNC_INVENTORY = new CustomPayload.Id<>(ZynCraft.id("sync_inventory"));

    public static void initialize(){
        PayloadTypeRegistry.playS2C().register(ZynCraftPackets.SYNC_INVENTORY, SyncInventoryPacket.CODEC);
    }
}
