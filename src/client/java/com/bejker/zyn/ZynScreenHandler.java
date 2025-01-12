package com.bejker.zyn;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.util.Identifier;

import static com.bejker.zyn.inventory.ZynInventory.SLOT_X;
import static com.bejker.zyn.inventory.ZynInventory.SLOT_Y;

public class ZynScreenHandler {
    private static final Identifier SLOT_TEXTURE = ZynCraft.id("textures/gui/zyn_slot.png");
    public static void render(DrawContext context,int x,int y,int width,int height, int mouseX, int mouseY, float delta) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        assert screen != null;
        x = x - 20;
        y = y + 5;
        //context.drawGuiTexture(RenderLayer::getGuiTextured, SLOT_TEXTURE, x, y, 16, 16);
        //RenderSystem.setShader(MinecraftClient.getInstance().gameRenderer.);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SLOT_TEXTURE);
        context.drawTexture(RenderLayer::getGuiTextured,SLOT_TEXTURE,x,y,0,0,20,20,20,20);
    }
}
