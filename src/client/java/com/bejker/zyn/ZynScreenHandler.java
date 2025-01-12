package com.bejker.zyn;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import static com.bejker.zyn.inventory.ZynInventory.SLOT_X;
import static com.bejker.zyn.inventory.ZynInventory.SLOT_Y;

public class ZynScreenHandler {
    private static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("container/slot/potioan");
    public static void render(DrawContext context,int x,int y,int width,int height, int mouseX, int mouseY, float delta) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        assert screen != null;
        x = x - 16;
        y = y + 7;
        context.drawGuiTexture(RenderLayer::getGuiTextured, SLOT_TEXTURE, x, y, 16, 16);
    }
}
