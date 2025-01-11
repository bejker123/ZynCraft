package com.bejker.zyn.mixin;

import com.bejker.zyn.network.SyncInventoryPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.Whitelist;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static com.bejker.zyn.ZynCraft.ZYN_SLOT;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect", at = @At("TAIL"))
    void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){

        Map<Integer, NbtCompound> tag = new HashMap<>();
        ItemStack stack = player.getInventory().getStack(ZYN_SLOT);
        if (stack == ItemStack.EMPTY){
            return;
        }

        Map<Integer, ItemStack> stacks = new HashMap<>(Map.of());
        stacks.put(ZYN_SLOT,stack);
        ServerPlayNetworking.send(player, new SyncInventoryPacket(player.getId(),stacks , tag));
    }
}
