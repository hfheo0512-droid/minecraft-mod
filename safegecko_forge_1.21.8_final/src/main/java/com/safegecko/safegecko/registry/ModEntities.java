package com.safegecko.safegecko.registry;

import com.safegecko.safegecko.SafeGecko;
import com.safegecko.safegecko.entity.StalkerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SafeGecko.MODID);

    public static final RegistryObject<EntityType<StalkerEntity>> STALKER =
            ENTITIES.register("stalker", () ->
                    EntityType.Builder.of(StalkerEntity::new, MobCategory.MONSTER)
                            .sized(0.7F, 2.1F)
                            .build("stalker")
            );

    @Mod.EventBusSubscriber(modid = SafeGecko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEntityEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(STALKER.get(), StalkerEntity.createAttributes().build());
        }
    }
}
