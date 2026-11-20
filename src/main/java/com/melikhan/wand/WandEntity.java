package com.melikhan.wand;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Oyuncunun bindigi ucan asa. Tekne gibi calisir: ucusu oyuncunun kendi bilgisayari (istemci)
 * hesaplar, sunucu konumu kabul eder. Boylece kamera akici olur.
 */
public class WandEntity extends Entity {
	private static final EntityDataAccessor<Boolean> DIVING =
			SynchedEntityData.defineId(WandEntity.class, EntityDataSerializers.BOOLEAN);

	private boolean clientDiving; // sadece istemcide
	private int emptyTicks;       // sadece sunucuda

	public WandEntity(EntityType<? extends WandEntity> type, Level level) {
		super(type, level);
		this.setNoGravity(true);
		this.setInvulnerable(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DIVING, false);
	}

	public boolean isDiving() {
		return this.entityData.get(DIVING);
	}

	private @Nullable Player getRider() {
		return this.getFirstPassenger() instanceof Player p ? p : null;
	}

	@Override
	public @Nullable LivingEntity getControllingPassenger() {
		return getRider();
	}

	@Override
	protected boolean canAddPassenger(Entity passenger) {
		return this.getPassengers().isEmpty() && passenger instanceof Player;
	}

	@Override
	public void tick() {
		super.tick();
		Player rider = getRider();

		if (!this.level().isClientSide()) {
			if (rider == null) {
				if (++emptyTicks > 5) {
					this.discard();
				}
				return;
			}
			emptyTicks = 0;
			rider.resetFallDistance();
			this.resetFallDistance();
			boolean now = isDiving();
			boolean next = WandConfig.nextDiveState(now, this.getXRot());
			if (now != next) {
				this.entityData.set(DIVING, next);
			}
			return;
		}

		// ---- Istemci: sadece binen oyuncunun kendi bilgisayari asayi hareket ettirir ----
		if (rider != null && rider.isLocalPlayer()) {
			controlFromRider(rider);
		}
	}

	private void controlFromRider(Player rider) {
		// Asanin durusu oyuncunun bakisini biraz gecikmeyle takip eder (yumusak gorunum).
		float targetPitch = Mth.clamp(rider.getXRot(), WandConfig.MAX_PITCH_UP, 90.0f);
		float yawDiff = Mth.wrapDegrees(rider.getYRot() - this.getYRot());
		this.setYRot(this.getYRot() + yawDiff * 0.4f);
		this.setXRot(this.getXRot() + (targetPitch - this.getXRot()) * 0.4f);

		clientDiving = WandConfig.nextDiveState(clientDiving, this.getXRot());
		double speed = clientDiving ? WandConfig.DIVE_SPEED : WandConfig.GLIDE_SPEED;

		// Hareket yonu: oyuncunun baktigi yon
		Vec3 look = Vec3.directionFromRotation(targetPitch, rider.getYRot());
		Vec3 velocity = this.getDeltaMovement().lerp(look.scale(speed), 0.2);
		this.setDeltaMovement(velocity);
		this.move(MoverType.SELF, velocity);
		if (this.horizontalCollision || this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
		}
	}

	/**
	 * Oyuncunun oturdugu nokta: asanin "ust" yonu, asa asagi egildikce one dogru doner.
	 * (Ucak asagi burun yaptiginda tepesi one bakar.)
	 */
	@Override
	protected void positionRider(Entity passenger, MoveFunction moveFunction) {
		if (!this.hasPassenger(passenger)) {
			return;
		}
		double yaw = Math.toRadians(this.getYRot());
		double pitch = Math.toRadians(this.getXRot());
		double sin = Math.sin(pitch);
		double upX = sin * -Math.sin(yaw);
		double upY = Math.cos(pitch);
		double upZ = sin * Math.cos(yaw);
		double seat = WandConfig.SEAT_HEIGHT;
		moveFunction.accept(passenger,
				this.getX() + upX * seat,
				this.getY() + upY * seat - WandConfig.RIDER_HIP_HEIGHT,
				this.getZ() + upZ * seat);
		if (passenger instanceof LivingEntity living) {
			living.setYBodyRot(this.getYRot());
		}
	}

	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (!this.level().isClientSide() && passenger instanceof LivingEntity living) {
			living.resetFallDistance();
			if (WandConfig.SLOW_FALL_TICKS > 0) {
				living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
						WandConfig.SLOW_FALL_TICKS, 0, false, false, true));
			}
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
