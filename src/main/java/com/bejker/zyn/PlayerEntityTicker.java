package com.bejker.zyn;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.bejker.zyn.items.ZynItem.NICOTINE_CONTENT;

//TODO: implement for other entities
public class PlayerEntityTicker {


    //In L
    private static final float BLOOD_VOLUME = 5.0f;

    //TODO: find the right factor
    //In nmol
    private static final float SCALING_FACTOR = 0.01f;

    //In nmol/L
    private static float calculate_nicotine_concentration(int content_){
       float content = ((float) content_);
       return content * SCALING_FACTOR / BLOOD_VOLUME;
    }
    private static void handle_nicotine_content(ServerPlayerEntity player){
        Integer attached = player.getAttached(NICOTINE_CONTENT);
        if(attached == null || attached == 0){
            return;
        }
        if(player.getWorld().getTime() % 3 == 0){
            attached -= 1;
        }
        player.setAttached(NICOTINE_CONTENT,attached);
        float concentration = calculate_nicotine_concentration(attached);
        player.sendMessage(Text.literal(String.format("%.3f",concentration)+" nmol/L"),true);
    }
    public static void tick(PlayerEntity player_){
        if(player_ instanceof ServerPlayerEntity player){
            handle_nicotine_content(player);
        }
    }
}
