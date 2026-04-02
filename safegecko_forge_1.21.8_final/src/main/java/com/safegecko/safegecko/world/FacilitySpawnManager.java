package com.safegecko.safegecko.world;

import com.safegecko.safegecko.entity.StalkerEntity;
import com.safegecko.safegecko.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class FacilitySpawnManager {

    private static final int CHECK_INTERVAL = 100;
    private static final int MAX_STALKERS_NEAR_PLAYER = 3;

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % CHECK_INTERVAL != 0) return;

        List<ServerPlayer> players = level.players();

        for (ServerPlayer player : players) {
            if (player.isCreative() || player.isSpectator()) continue;

            BlockPos playerPos = player.blockPosition();

            if (!FacilityZoneTracker.isInsideFacility(level, playerPos)) continue;

            int nearbyCount = level.getEntitiesOfClass(
                    StalkerEntity.class,
                    player.getBoundingBox().inflate(40)
            ).size();

            if (nearbyCount >= MAX_STALKERS_NEAR_PLAYER) continue;

            BlockPos spawnPos = findSpawnPos(level, player);

            if (spawnPos != null) {
                StalkerEntity stalker = ModEntities.STALKER.get().create(level);
                if (stalker != null) {
                    stalker.moveTo(
                            spawnPos.getX() + 0.5,
                            spawnPos.getY(),
                            spawnPos.getZ() + 0.5,
                            level.random.nextFloat() * 360F,
                            0F
                    );
                    level.addFreshEntity(stalker);
                }
            }
        }
    }

    private static BlockPos findSpawnPos(ServerLevel level, ServerPlayer player) {
        BlockPos origin = player.blockPosition();

        for (int i = 0; i < 30; i++) {
            int dx = level.random.nextInt(30) - 15;
            int dz = level.random.nextInt(30) - 15;
            int dy = level.random.nextInt(6) - 3;

            BlockPos pos = origin.offset(dx, dy, dz);

            if (pos.closerThan(origin, 12)) continue;
            if (!level.getBlockState(pos).isAir()) continue;
            if (!level.getBlockState(pos.above()).isAir()) continue;
            if (level.getBlockState(pos.below()).isAir()) continue;
            if (!FacilityZoneTracker.isInsideFacility(level, pos)) continue;
            if (level.canSeeSky(pos)) continue;

            double lookX = player.getLookAngle().x;
            double lookZ = player.getLookAngle().z;
            double dirX = pos.getX() - player.getX();
            double dirZ = pos.getZ() - player.getZ();
            double dot = lookX * dirX + lookZ * dirZ;

            if (dot > 0) continue;

            return pos;
        }

        return null;
    }
}
