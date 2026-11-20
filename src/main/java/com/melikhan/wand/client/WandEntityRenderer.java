package com.melikhan.wand.client;

import com.melikhan.wand.MelikhanWandMod;
import com.melikhan.wand.ModItems;
import com.melikhan.wand.WandConfig;
import com.melikhan.wand.WandEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

/**
 * Asayi cizer. Iki hazir model tanimi var (assets/melikhan/items/):
 *  melikhan_glide -> kanatlar acik
 *  melikhan_dive  -> dalis kanatlari
 */
public class WandEntityRenderer extends EntityRenderer<WandEntity, WandRenderState> {
	// Blockbench modelinde: Y = asa govdesi (bas yukarida), X = kalinlik, Z = kanat acikligi.
	// Ucus duzeninde: Y -> ileri, X -> yukari, Z -> sol. Bu, (1,1,1) ekseni etrafinda 120 derecedir.
	private static final Quaternionf MODEL_TO_FLIGHT =
			new Quaternionf().rotationAxis((float) Math.toRadians(120.0), 0.57735027f, 0.57735027f, 0.57735027f);

	public WandEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.0f;
	}

	@Override
	public WandRenderState createRenderState() {
		return new WandRenderState();
	}

	@Override
	public void extractRenderState(WandEntity entity, WandRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.wandYaw = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
		state.wandPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
		// Oyuncunun kendi asasinin kopyasi; sadece model suzulme/dalis kanatli surumune cevrilir.
		ItemStack stack = entity.getDisplayStack().copy();
		if (stack.isEmpty()) {
			stack = new ItemStack(ModItems.MELIKHAN);
		}
		stack.set(DataComponents.ITEM_MODEL,
				MelikhanWandMod.id(entity.isDiving() ? "melikhan_dive" : "melikhan_glide"));
		Minecraft.getInstance().getItemModelResolver()
				.updateForNonLiving(state.item, stack, ItemDisplayContext.NONE, entity);
	}

	@Override
	public void submit(WandRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
			CameraRenderState camera) {
		super.submit(state, poseStack, collector, camera);
		if (state.item.isEmpty()) {
			return;
		}
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(-state.wandYaw));
		poseStack.mulPose(Axis.XP.rotationDegrees(state.wandPitch));
		if (!WandConfig.HEAD_POINTS_FORWARD) {
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
		}
		poseStack.mulPose(Axis.ZP.rotationDegrees(WandConfig.MODEL_ROLL_DEGREES));
		poseStack.mulPose(MODEL_TO_FLIGHT);
		// Oturma noktasini orijine getir (model birimi 1/16 blok, modelin merkezi 8,8,8).
		poseStack.translate(0.5f - WandConfig.PIVOT_X / 16.0f,
				0.5f - WandConfig.PIVOT_Y / 16.0f,
				0.5f - WandConfig.PIVOT_Z / 16.0f);
		state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		poseStack.popPose();
	}
}
