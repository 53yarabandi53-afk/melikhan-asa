package com.melikhan.wand;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WandItem extends Item {
	public WandItem(Properties properties) {
		super(properties);
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
				// Ikinci sag tik: ucusu bitir.
				player.stopRiding();
				wand.discard();
			} else if (!player.isPassenger()) {
				// Ilk sag tik: asa olustur ve oyuncuyu bindir.
				WandEntity wand = new WandEntity(ModEntities.WAND, level);
				wand.setPos(player.getX(),
						player.getY() + WandConfig.RIDER_HIP_HEIGHT - WandConfig.SEAT_HEIGHT,
						player.getZ());
				wand.setYRot(player.getYRot());
				wand.setXRot(0.0f);
				level.addFreshEntity(wand);
				player.resetFallDistance();
				player.startRiding(wand);
			}
		}
		return InteractionResult.SUCCESS;
	}
}
