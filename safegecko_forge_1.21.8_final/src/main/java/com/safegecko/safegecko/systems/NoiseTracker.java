package com.safegecko.safegecko.systems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoiseTracker {

    public static class NoiseData {
        public final BlockPos pos;
        public final int strength;
        public final long gameTime;

        public NoiseData(BlockPos pos, int strength, long gameTime) {
            this.pos = pos;
            this.strength = strength;
            this.gameTime = gameTime;
        }
    }

    private static final Map<UUID, NoiseData> LAST_NOISE = new HashMap<>();

    public static void makeNoise(ServerPlayer player, int strength) {
        ServerLevel level = player.serverLevel();
        LAST_NOISE.put(player.getUUID(), new NoiseData(
                player.blockPosition(),
                strength,
                level.getGameTime()
        ));
    }

    public static NoiseData getNoise(UUID playerId) {
        return LAST_NOISE.get(playerId);
    }

    public static boolean isNoiseFresh(ServerLevel level, NoiseData data, int maxAgeTicks) {
        if (data == null) return false;
        return (level.getGameTime() - data.gameTime) <= maxAgeTicks;
    }
}
