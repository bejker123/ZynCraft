package com.bejker.zyn;

import com.bejker.zyn.commands.ZynCraftCommands;
import com.bejker.zyn.items.ZynCraftItems;
import com.bejker.zyn.network.ZynCraftPackets;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZynCraft implements ModInitializer {
	public static final String MOD_ID = "zyncraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID,id);
	}

	@Override
	public void onInitialize() {
		ZynCraftItems.initialize();
		ZynCraftPackets.initialize();
		ZynCraftCommands.initialize();
	}

}