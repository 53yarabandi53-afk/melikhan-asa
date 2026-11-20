package com.melikhan.wand;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class MelikhanWandMod implements ModInitializer {
	public static final String MOD_ID = "melikhan";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();
	}
}
