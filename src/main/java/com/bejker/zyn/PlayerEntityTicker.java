package com.bejker.zyn;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.bejker.zyn.items.ZynItem.NICOTINE_CONTENT;

//TODO: implement for other entities
public class PlayerEntityTicker {

    //Max concentration threshold in nmol/L
    private static final float MAX_CONC = 2*444.f;
    //Min concentration threshold in nmol/L
    private static final float MIN_CONC =  25.f;

    //In L
    private static final float BLOOD_VOLUME = 5.0f;

    //TODO: Check balancing
    //In nmol
    private static final float SCALING_FACTOR = 0.0148f;

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
        int interval = (int) Math.floor(concentration / MIN_CONC);
        player.sendMessage(Text.literal(String.format("%.3f",concentration)+" nmol/L; "+attached+" interval: "+interval).formatted(Formatting.GOLD),true);
        if(interval >= 1){
            if(!player.hasStatusEffect(StatusEffects.HASTE)){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE,400,1),player);
            }
            if(interval >= 2){
                if(!player.hasStatusEffect(StatusEffects.SPEED)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 1), player);
                }
                if(interval >= 3){
                    if(!player.hasStatusEffect(StatusEffects.STRENGTH)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, 1), player);
                    }
                    if(interval >= 17){
                        if(!player.hasStatusEffect(StatusEffects.NAUSEA)) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 2), player);
                        }
                        if(concentration >= MAX_CONC){
                            player.setAttached(NICOTINE_CONTENT,0);
                            player.kill((ServerWorld) player.getWorld());
                        }
                    }
                }
            }
        }
    }
    public static void tick(PlayerEntity player_){
        if(player_ instanceof ServerPlayerEntity player){
            handle_nicotine_content(player);
        }
    }
}
