package com.bejker.zyn;

import com.bejker.zyn.commands.ResetNicotineCommand;
import com.bejker.zyn.commands.SetNicotineCommand;
import com.bejker.zyn.commands.ZynCraftCommands;
import com.bejker.zyn.items.ZynCraftItems;
import com.bejker.zyn.network.SyncInventoryPacket;
import com.bejker.zyn.network.ZynCraftPackets;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.bejker.zyn.items.ZynItem.NICOTINE_CONTENT;
import static net.minecraft.server.command.CommandManager.*;

public class ZynCraft implements ModInitializer {
	public static final String MOD_ID = "zyncraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final int ZYN_SLOT = 45;

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID,id);
	}

	public static Set<Item> ZynabbleItems = new HashSet<>();

	static{
		ZynabbleItems.add(ZynCraftItems.ZYN);
	}
	public static boolean canBePlacedInZynSlot(ItemStack stack){
		return ZynabbleItems.contains(stack.getItem());
	}

	public static void reset_nicotine(ServerCommandSource source, ServerPlayerEntity player){
		set_nicotine(source,player,0);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ZynCraftItems.initialize();
		PayloadTypeRegistry.playS2C().register(ZynCraftPackets.SYNC_INVENTORY, SyncInventoryPacket.CODEC);

		ZynCraftCommands.initialize();
	}

	public static void set_nicotine(ServerCommandSource source, ServerPlayerEntity player, int value) {
		player.setAttached(NICOTINE_CONTENT,value);
		source.sendFeedback(() -> Text.literal("Set nicotine for ")
		.append(
		Text.literal(player.getName().getLiteralString()).formatted(Formatting.AQUA))
				.append(Text.literal(" to: "))
				.append(Text.of(String.valueOf(value).formatted(Formatting.GOLD))), false);

	}
}