package com.bejker.zyn;

import com.bejker.zyn.inventory.ZynSlot;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.awt.*;

import static com.bejker.zyn.inventory.ZynInventory.SLOT_X;
import static com.bejker.zyn.inventory.ZynInventory.SLOT_Y;

public class ZynScreenHandler {
    private static final Identifier SLOT_TEXTURE = ZynCraft.id("textures/gui/zyn_slot.png");

    private static final Identifier SLOT_HIGHLIGHT_BACK_TEXTURE = Identifier.ofVanilla("container/slot_highlight_back");
    private static final Identifier SLOT_HIGHLIGHT_FRONT_TEXTURE = Identifier.ofVanilla("container/slot_highlight_front");

    public static void render(DrawContext context, Slot focused_slot) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        assert screen != null;
        int x = -20;
        int y = 5;
        //RenderSystem.setShader(MinecraftClient.getInstance().gameRenderer.);
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 1.0f);
        context.drawTexture(i->{
            return RenderLayer.getGuiTextured(i);
        },SLOT_TEXTURE,0,0,0,0,20,20,20,20);
        if(focused_slot != null&&focused_slot.id == ZynCraft.ZYN_SLOT){
            x = -2;
            y = -2;
            context.drawGuiTexture(RenderLayer::getGuiTexturedOverlay, SLOT_HIGHLIGHT_FRONT_TEXTURE, x, y, 24, 24);
            context.drawGuiTexture(RenderLayer::getGuiTextured, SLOT_HIGHLIGHT_BACK_TEXTURE, x, y, 24, 24);
        }
        context.getMatrices().pop();
    }
}
