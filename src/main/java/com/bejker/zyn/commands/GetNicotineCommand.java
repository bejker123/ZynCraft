package com.bejker.zyn.commands;

import com.bejker.zyn.items.ZynItem;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class GetNicotineCommand {
    public static void initialize(){

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("get_nicotine")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context ->{
                            if(context.getSource().getEntity() instanceof ServerPlayerEntity){
                                int nicotine = ZynItem.get_nicotine(context.getSource().getPlayer());
                                context.getSource().sendFeedback(
                                        () -> Text.literal("Nicotine for ").append(
                                                        Text.literal(context.getSource().getName()).formatted(Formatting.AQUA)
                                                ).append(
                                                        Text.literal(" ")
                                                )
                                                .append(
                                                        Text.literal(String.valueOf(nicotine)).formatted(Formatting.GOLD)
                                                ),false);
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
                                    final ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                    if(player == null){
                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Failed to find player!")
                                                        .formatted(Formatting.RED), false);
                                        return 0;
                                    }
                                    int nicotine = ZynItem.get_nicotine(player);
                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Nicotine for ").append(
                                                    Text.literal(context.getSource().getName()).formatted(Formatting.AQUA)
                                            ).append(
                                                   Text.literal(" ")
                                            )
                                            .append(
                                                    Text.literal(String.valueOf(nicotine)).formatted(Formatting.GOLD)
                                            ),false);
                                    return 1;
                                }))
        ));
    }
}
