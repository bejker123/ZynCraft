package com.bejker.zyn.network;

import com.bejker.zyn.ZynCraft;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class ZynCraftPackets {
    public static final CustomPayload.Id<SyncInventoryPacket> SYNC_INVENTORY = new CustomPayload.Id<>(ZynCraft.id("sync_inventory"));
}
