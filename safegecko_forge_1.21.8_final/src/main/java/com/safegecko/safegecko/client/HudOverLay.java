package com.safegecko.safegecko.client;

import com.safegecko.safegecko.systems.FearSystem;
import com.safegecko.safegecko.systems.StaminaSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public class HudOverlay {

    public static void render(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.options.hideGui) return;

        int fear = 0;
        int stamina = 100;

        // 싱글플레이 기준 HUD 동기화
        if (mc.getSingleplayerServer() != null) {
            var serverPlayer = mc.getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
            if (serverPlayer != null) {
                fear = FearSystem.getFear(serverPlayer);
                stamina = StaminaSystem.getStamina(serverPlayer);
            }
        }

        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 왼쪽 아래
        int x = 8;
        int y = screenHeight - 22;

        drawMinimalBar(guiGraphics, fear, 100, x, y, 0xAA0000);      // FEAR
        drawMinimalBar(guiGraphics, stamina, 100, x, y + 8, 0x00AA00); // STAMINA
    }

    private static void drawMinimalBar(GuiGraphics guiGraphics, int value, int max, int x, int y, int fillColor) {
        int barWidth = 48;
        int barHeight = 4;
        int fill = (int) ((value / (float) max) * barWidth);

        // 배경
        guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0x55000000);

        // 채움
        guiGraphics.fill(x, y, x + fill, y + barHeight, 0xFF000000 | fillColor);

        // 아주 얇은 테두리
        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y, 0x66FFFFFF);
        guiGraphics.fill(x - 1, y + barHeight, x + barWidth + 1, y + barHeight + 1, 0x66FFFFFF);
        guiGraphics.fill(x - 1, y, x, y + barHeight, 0x66FFFFFF);
        guiGraphics.fill(x + barWidth, y, x + barWidth + 1, y + barHeight, 0x66FFFFFF);
    }
}