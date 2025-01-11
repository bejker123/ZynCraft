package com.bejker.zyn;

import com.bejker.zyn.network.SyncInventoryPacket;
import com.bejker.zyn.network.ZynCraftPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bejker.zyn.ZynCraft.MOD_ID;

public class ZynCraftClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID+"_client");

	private static void receiveSyncInventory(SyncInventoryPacket payload, ClientPlayNetworking.Context context) {
		MinecraftClient client = context.client();
		if(client == null){
			return;
		}
		if(client.world == null){
			return;
		}

		Entity entity = client.world.getEntityById(payload.entityId());
		if (entity instanceof PlayerEntity player) {
			PlayerInventory inventory = player.getInventory();
			for (var i : payload.contentUpdates().entrySet()) {
				int slot = i.getKey();
				ItemStack stack = i.getValue();
				inventory.removeStack(slot);
				inventory.insertStack(slot, stack);
				//LOGGER.info("{}", player.getInventory().getStack(slot));
			}
			//for (var i : payload.inventoryUpdates().entrySet()){
			//	int slot = i.getKey();
			//	NbtCompound compound = i.getValue();
			//player.getInventory().getStack(slot).
			//player.getInventory().insertStack(slot,stack);
			//}
		}
	}

	public static Slot getZynSlot() {
        assert MinecraftClient.getInstance().player != null;
        ZynInventory inventory = (ZynInventory) MinecraftClient.getInstance().player.getInventory();
        //LOGGER.info("SLOT: {}, ID: {}",slot,slot.id);
		return inventory.zynCraft$getZynSlot();
	}

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ZynCraftPackets.SYNC_INVENTORY, ZynCraftClient::receiveSyncInventory);
	}
}