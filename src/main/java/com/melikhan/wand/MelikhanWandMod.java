package com.melikhan.wand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public class MelikhanWandMod implements ModInitializer {
	public static final String MOD_ID = "melikhan";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();

		// Guvenlik agi: oyuncu ucus sirasinda oyundan cikarsa asa gizli kalmasin.
		// Her saniye, asada degilken gizli kalmis asalari tekrar gorunur yapar.
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (server.getTickCount() % 20 != 0) {
				return;
			}
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (!(player.getVehicle() instanceof WandEntity)) {
					WandItem.restoreAll(player);
				}
			}
		});
	}
}
