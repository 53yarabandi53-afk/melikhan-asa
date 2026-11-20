package com.melikhan.wand;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Asa elde tutulan ayni ItemStack olarak KALIR (kaybolmaz, kopyalanmaz).
 * Ucus sirasinda sadece elde gorunen modeli kapatilir; ayni asanin modeli oyuncunun altindaki
 * binek entity'sinde cizilir. Ucus bitince modeli geri acilir.
 */
public class WandItem extends Item {
	public static final Identifier MODEL_NORMAL = MelikhanWandMod.id("melikhan");
	public static final Identifier MODEL_HIDDEN = MelikhanWandMod.id("melikhan_hidden");

	public WandItem(Properties properties) {
		super(properties);
	}

	public static boolean isHidden(ItemStack stack) {
		return MODEL_HIDDEN.equals(stack.get(DataComponents.ITEM_MODEL));
	}

	public static void setHidden(ItemStack stack, boolean hidden) {
		stack.set(DataComponents.ITEM_MODEL, hidden ? MODEL_HIDDEN : MODEL_NORMAL);
	}

	/** Oyuncunun envanterindeki butun gizlenmis asalarin gorunumunu geri acar. */
	public static void restoreAll(Player player) {
		Inventory inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.getItem() instanceof WandItem && isHidden(stack)) {
				setHidden(stack, false);
			}
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.getCooldowns().isOnCooldown(stack)) {
			return InteractionResult.FAIL;
		}
		player.getCooldowns().addCooldown(stack, 8);

		if (!level.isClientSide()) {
			if (player.getVehicle() instanceof WandEntity wand) {
				// Ikinci sag tik: ucusu bitir. (Asa geri gorunur hale gelir, removePassenger icinde.)
				player.stopRiding();
				wand.discard();
			} else if (!player.isPassenger()) {
				// Ilk sag tik: elindeki asanin gorunumunu kapat, ayni modeli ayaklarinin altinda goster.
				WandEntity wand = new WandEntity(ModEntities.WAND, level);
				wand.setDisplayStack(stack.copy()); // sadece gorsel kopya; envantere hic girmez
				wand.setHand(hand);
				wand.setPos(player.getX(),
						player.getY() + WandConfig.RIDER_HIP_HEIGHT - WandConfig.SEAT_HEIGHT,
						player.getZ());
				wand.setYRot(player.getYRot());
				wand.setXRot(0.0f);
				level.addFreshEntity(wand);
				player.resetFallDistance();
				if (player.startRiding(wand)) {
					setHidden(stack, true);
				} else {
					wand.discard();
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
}
