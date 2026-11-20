package com.melikhan.wand.client;

import com.melikhan.wand.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MelikhanWandClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRenderers.register(ModEntities.WAND, WandEntityRenderer::new);
	}
}
