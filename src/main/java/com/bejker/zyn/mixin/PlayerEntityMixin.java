package com.bejker.zyn.mixin;

import com.bejker.zyn.PlayerEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    void tick(CallbackInfo ci){
        PlayerEntityTicker.tick((PlayerEntity) (((Object) this)));
    }
}
