package com.safegecko.safegecko.systems;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FearSystem {

    private static final Map<UUID, Integer> FEAR = new HashMap<>();
    public static final int MAX_FEAR = 100;

    public static int getFear(ServerPlayer player) {
        return FEAR.getOrDefault(player.getUUID(), 0);
    }

    public static void addFear(ServerPlayer player, int amount) {
        FEAR.put(player.getUUID(), Math.min(MAX_FEAR, getFear(player) + amount));
    }

    public static void reduceFear(ServerPlayer player, int amount) {
        FEAR.put(player.getUUID(), Math.max(0, getFear(player) - amount));
    }

    public static boolean isPanicking(ServerPlayer player) {
        return getFear(player) >= 80;
    }
}
