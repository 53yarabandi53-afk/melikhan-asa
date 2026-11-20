package com.melikhan.wand;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {
	private ModEntities() {}

	public static final ResourceKey<EntityType<?>> WAND_KEY =
			ResourceKey.create(Registries.ENTITY_TYPE, MelikhanWandMod.id("wand"));

	public static final EntityType<WandEntity> WAND = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			WAND_KEY,
			EntityType.Builder.<WandEntity>of(WandEntity::new, MobCategory.MISC)
					.sized(0.9f, 0.5f)
					.clientTrackingRange(10)
					// DIKKAT: .noSave() KULLANMA! Minecraft, kaydedilemeyen (noSave) bir varliga binmeyi
					// sunucuda reddeder (startRiding false doner). Oyuncu tipi de noSave oldugu icin
					// asa kaydedilebilir olmak zorunda.
					.build(WAND_KEY));

	public static void register() {
		// Sinifin yuklenmesi kaydi yapmak icin yeterli.
	}
}
