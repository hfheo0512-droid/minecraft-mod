package com.safegecko.safegecko.systems;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaminaSystem {

    private static final Map<UUID, Integer> STAMINA = new HashMap<>();
    public static final int MAX_STAMINA = 100;

    public static int getStamina(ServerPlayer player) {
        return STAMINA.getOrDefault(player.getUUID(), MAX_STAMINA);
    }

    public static void setStamina(ServerPlayer player, int value) {
        STAMINA.put(player.getUUID(), Math.max(0, Math.min(MAX_STAMINA, value)));
    }

    public static void drain(ServerPlayer player, int amount) {
        setStamina(player, getStamina(player) - amount);
    }

    public static void recover(ServerPlayer player, int amount) {
        setStamina(player, getStamina(player) + amount);
    }

    public static boolean isExhausted(ServerPlayer player) {
        return getStamina(player) <= 0;
    }
}
