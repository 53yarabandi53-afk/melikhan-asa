package com.melikhan.wand;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public final class ModItems {
	private ModItems() {}

	public static final ResourceKey<Item> MELIKHAN_KEY =
			ResourceKey.create(Registries.ITEM, MelikhanWandMod.id("melikhan"));

	// Saldiri hasari 1: oyuncunun taban hasari zaten 1 oldugu icin ek hasar 0.
	// Uretim tarifi YOK (data/melikhan/recipe klasoru hic olusturulmadi).
	public static final Item MELIKHAN = Registry.register(
			BuiltInRegistries.ITEM,
			MELIKHAN_KEY,
			new WandItem(new Item.Properties()
					.setId(MELIKHAN_KEY)
					.stacksTo(1)
					.rarity(Rarity.EPIC)
					.attributes(ItemAttributeModifiers.builder()
							.add(Attributes.ATTACK_DAMAGE,
									new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0,
											AttributeModifier.Operation.ADD_VALUE),
									EquipmentSlotGroup.MAINHAND)
							.build())));

	public static void register() {
		// Sadece Creative envanterinde (Araclar sekmesi) gorunur.
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
				.register(output -> output.accept(MELIKHAN));
	}
}
