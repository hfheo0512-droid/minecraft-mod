package com.safegecko.safegecko.events;

import com.safegecko.safegecko.SafeGecko;
import com.safegecko.safegecko.systems.FearSystem;
import com.safegecko.safegecko.systems.NoiseTracker;
import com.safegecko.safegecko.systems.StaminaSystem;
import com.safegecko.safegecko.world.FacilitySpawnManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SafeGecko.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (!(event.level instanceof ServerLevel level)) return;
        if (event.phase != TickEvent.Phase.END) return;

        FacilitySpawnManager.tick(level);

        for (ServerPlayer player : level.players()) {
            if (player.isCreative() || player.isSpectator()) continue;

            if (player.isSprinting() && player.onGround()) {
                StaminaSystem.drain(player, 1);

                if (player.tickCount % 10 == 0) {
                    NoiseTracker.makeNoise(player, 4);
                }

                if (StaminaSystem.isExhausted(player)) {
                    player.setSprinting(false);
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
                }
            } else {
                int recoverAmount = FearSystem.isPanicking(player) ? 0 : 1;
                if (player.tickCount % 5 == 0) {
                    StaminaSystem.recover(player, recoverAmount);
                }
            }

            if (player.fallDistance > 1.5F && player.onGround() && player.tickCount % 5 == 0) {
                NoiseTracker.makeNoise(player, 3);
            }

            if (player.tickCount % 40 == 0) {
                FearSystem.reduceFear(player, 1);
            }

            int fear = FearSystem.getFear(player);

            if (fear >= 80) {
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, false, false));
            } else if (fear >= 50) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
            }
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            NoiseTracker.makeNoise(player, 8);
            FearSystem.addFear(player, 1);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            NoiseTracker.makeNoise(player, 5);
        }
    }
}
