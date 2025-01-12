package com.bejker.zyn.mixin.client;

import com.bejker.zyn.ZynScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    /*
        @Unique
        protected void drawSlot(DrawContext context, Slot slot) {
            int i = slot.x;
            int j = slot.y;
            ItemStack itemStack = slot.getStack();
            String string = null;
            int k;
            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, 100.0F);
            if (itemStack.isEmpty() && slot.isEnabled()) {
                Identifier identifier = slot.getBackgroundSprite();
                if (identifier != null) {
                    context.drawGuiTexture(RenderLayer::getGuiTextured, identifier, i, j, 16, 16);
                }
            }

            context.fill(i, j, i + 16, j + 16, -2130706433);

            k = slot.x + slot.y * this.backgroundWidth;
            if (slot.disablesDynamicDisplay()) {
                context.drawItemWithoutEntity(itemStack, i, j, k);
            } else {
                context.drawItem(itemStack, i, j, k);
            }

            context.drawStackOverlay(this.client.t, itemStack, i, j, string);

            context.getMatrices().pop();
        }
        */
    @Inject(method = "init", at = @At("RETURN"))
    void init(CallbackInfo ci){

    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
    }
    @Inject(at = @At("RETURN"), method = "drawBackground")
    private void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
    }

    @Inject(at = @At("TAIL"), method = "drawForeground")
    private void drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo info) {
    }
}
