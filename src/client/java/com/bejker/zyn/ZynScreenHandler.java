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
    public static void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        assert screen != null;
        int x = screen.width + SLOT_X;
        int y = screen.height + SLOT_Y;
        context.drawGuiTexture(RenderLayer::getGuiTextured, SLOT_TEXTURE, x, y, 16, 16);
    }
}
