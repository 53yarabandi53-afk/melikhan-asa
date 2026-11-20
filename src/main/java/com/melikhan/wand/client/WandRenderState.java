package com.melikhan.wand.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

/** Cizim icin gereken bilgiler (26.1'de entity dogrudan cizicide kullanilmaz, "render state" kullanilir). */
public class WandRenderState extends EntityRenderState {
	public final ItemStackRenderState item = new ItemStackRenderState();
	public float wandYaw;
	public float wandPitch;
}
