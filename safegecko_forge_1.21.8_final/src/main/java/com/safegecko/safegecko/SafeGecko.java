package com.safegecko.safegecko;

import com.safegecko.safegecko.registry.ModEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SafeGecko.MODID)
public class SafeGecko {
    public static final String MODID = "safegecko";

    public SafeGecko() {
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
