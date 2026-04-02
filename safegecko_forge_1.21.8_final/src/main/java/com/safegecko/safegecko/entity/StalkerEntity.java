package com.safegecko.safegecko.entity;

import com.safegecko.safegecko.systems.FearSystem;
import com.safegecko.safegecko.systems.NoiseTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class StalkerEntity extends Monster {

    public StalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 34.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.29D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 36.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 12.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        List<ServerPlayer> players = serverLevel.players();

        for (ServerPlayer player : players) {
            double distToPlayer = this.distanceTo(player);

            if (distToPlayer < 14) {
                FearSystem.addFear(player, 1);
            }

            NoiseTracker.NoiseData noise = NoiseTracker.getNoise(player.getUUID());

            if (noise != null && NoiseTracker.isNoiseFresh(serverLevel, noise, 60)) {
                double dist = this.distanceToSqr(
                        noise.pos.getX() + 0.5,
                        noise.pos.getY(),
                        noise.pos.getZ() + 0.5
                );

                double hearRange = noise.strength * 8.0;
                if (dist <= hearRange * hearRange) {
                    this.getNavigation().moveTo(
                            noise.pos.getX() + 0.5,
                            noise.pos.getY(),
                            noise.pos.getZ() + 0.5,
                            1.2D
                    );

                    if (this.hasLineOfSight(player) && this.distanceTo(player) < 18) {
                        this.setTarget(player);
                    }
                }
            }
        }
    }
}
