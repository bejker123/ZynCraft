package com.bejker.zyn.commands;

import com.bejker.zyn.items.ZynItem;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetNicotineCommand {
    public static void initialize(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("set_nicotine")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(argument("value", IntegerArgumentType.integer(0))
                                .executes(context ->{
                                    final int value = IntegerArgumentType.getInteger(context,"value");
                                    if(context.getSource().getEntity() instanceof ServerPlayerEntity){
                                        ZynItem.set_nicotine(context.getSource(), Objects.requireNonNull(context.getSource().getPlayer()),value);
                                        return 1;
                                    }else{
                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Try calling as a player or adding an argument.")
                                                        .formatted(Formatting.RED), false);
                                        return 0;
                                    }
                                })
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            final int value = IntegerArgumentType.getInteger(context,"value");
                                            final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                            if(player == null){
                                                context.getSource().sendFeedback(
                                                        () -> Text.literal("Failed to find player!")
                                                                .formatted(Formatting.RED), false);
                                                return 0;
                                            }
                                            ZynItem.set_nicotine(context.getSource(),player,value);
                                            return 1;
                                        }))
                        )
        ));
    }
}