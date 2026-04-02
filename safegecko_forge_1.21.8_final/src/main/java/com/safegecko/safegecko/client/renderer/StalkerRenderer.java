package com.safegecko.safegecko.client.renderer;

import com.safegecko.safegecko.SafeGecko;
import com.safegecko.safegecko.entity.StalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;

public class StalkerRenderer extends ZombieRenderer {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SafeGecko.MODID, "textures/entity/stalker.png");

    public StalkerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.monster.Zombie entity) {
        return TEXTURE;
    }

    public ResourceLocation getTextureLocation(StalkerEntity entity) {
        return TEXTURE;
    }
}
