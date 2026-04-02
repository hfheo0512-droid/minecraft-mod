package com.safegecko.safegecko.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FacilityZoneTracker {

    public static boolean isInsideFacility(ServerLevel level, BlockPos center) {
        int score = 0;
        int radius = 6;

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-radius, -2, -radius),
                center.offset(radius, 3, radius)
        )) {
            BlockState state = level.getBlockState(pos);

            if (state.is(Blocks.IRON_BARS)) score += 3;
            if (state.is(Blocks.CHAIN)) score += 2;
            if (state.is(Blocks.CRACKED_STONE_BRICKS)) score += 2;
            if (state.is(Blocks.MOSSY_STONE_BRICKS)) score += 2;
            if (state.is(Blocks.DEEPSLATE_TILES)) score += 2;
            if (state.is(Blocks.DEEPSLATE_BRICKS)) score += 2;
            if (state.is(Blocks.HEAVY_CORE)) score += 3;
            if (state.is(Blocks.IRON_DOOR)) score += 2;
            if (state.is(Blocks.CHEST) || state.is(Blocks.BARREL)) score += 1;
        }

        boolean darkEnough = level.getMaxLocalRawBrightness(center) <= 7;
        boolean enclosed = !level.canSeeSky(center);

        return score >= 18 && darkEnough && enclosed;
    }
}
