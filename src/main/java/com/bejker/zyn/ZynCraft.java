package com.bejker.zyn;

import com.bejker.zyn.items.ZynCraftItems;
import com.bejker.zyn.network.SyncInventoryPacket;
import com.bejker.zyn.network.ZynCraftPackets;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
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

	Command<ServerCommandSource> command = context -> {
		return 0;
	};

	void reset_nicotine(ServerCommandSource source, ServerPlayerEntity player){
		player.setAttached(NICOTINE_CONTENT,0);
		source.sendFeedback(() -> Text.literal("Reset nicotine for ")
		.append(
		Text.literal(player.getName().getLiteralString()).formatted(Formatting.AQUA)), false);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ZynCraftItems.initialize();
		PayloadTypeRegistry.playS2C().register(ZynCraftPackets.SYNC_INVENTORY, SyncInventoryPacket.CODEC);
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
		literal("reset_nicotine")
		.requires(source -> source.hasPermissionLevel(2))
		.executes(context ->{
			if(context.getSource().getEntity() instanceof ServerPlayerEntity){
				context.getSource().sendFeedback(() -> Text.literal("Called /reset_nicotine with no arguments"), false);
				reset_nicotine(context.getSource(), Objects.requireNonNull(context.getSource().getPlayer()));
				return 1;
			}else{
				context.getSource().sendFeedback(() -> Text.literal("Try calling as a player or adding an argument."), false);
				return 0;
			}
			//return 1;
		})
		.then(argument("player", EntityArgumentType.player())
			.executes(context -> {
				// For versions below 1.19, replace "Text.literal" with "new LiteralText".
				// For versions below 1.20, remode "() ->" directly.
				final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
				if(player == null){
					context.getSource().sendFeedback(() -> Text.literal("Failed to find player!"), false);
					return 0;
				}
                reset_nicotine(context.getSource(),player);
				return 1;
			}))
		));
	}
}