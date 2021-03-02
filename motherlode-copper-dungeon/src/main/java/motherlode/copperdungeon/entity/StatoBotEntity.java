package motherlode.copperdungeon.entity;

import motherlode.copperdungeon.particle.ZapParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@SuppressWarnings("EntityConstructor")
public class StatoBotEntity extends HostileEntity implements Monster, IAnimatable {

	private final AnimationFactory animationFactory = new AnimationFactory(this);
	private int ticks;

	public StatoBotEntity (EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.ignoreCameraFrustum = true;
	}

	@Override
	protected void initGoals () {
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.175D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this));
	}

	@Override
	public void tick () {
		super.tick();
		ticks++;
		// Attack only every 3 seconds.
		PlayerEntity closestPlayer = this.world.getClosestPlayer(this, 3);
		if (closestPlayer != null && !this.world.isClient()) {
			if (ticks >= 60) {
				// Get closest player and attack
				if (!closestPlayer.isCreative()) {
					// Attack the player
					this.setTarget(closestPlayer);
					this.tryAttack(closestPlayer);
					this.setAttacking(true);
				}
				ticks = 0;
			}

			// Walk and face the player
			this.getNavigation().startMovingTo(closestPlayer, 0.250f);
			this.getLookControl().lookAt(closestPlayer, 30.0F, 30.0F);
		} else if (!this.world.isClient() && ticks >= 60 && closestPlayer != null) {
			MinecraftClient.getInstance().particleManager.addParticle(new ZapParticleEffect(this.getX(), this.getY(), this.getZ(), closestPlayer.getX(), closestPlayer.getY(), closestPlayer.getZ()), this.getX(), this.getY(), this.getZ(), 1.0F, 1.0F, 1.0F);
		} else {
			this.setAttacking(false);
		}
	}

	/*
	 * Controls animations of the mob.
	 */
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (!(lastLimbDistance > -0.15F && lastLimbDistance < 0.15F) && !this.isAttacking()) {
			// Assume they are walking
			event.getController().setAnimation(
				new AnimationBuilder().addAnimation("animation.Statobot.walk", true)
			);
			return PlayState.CONTINUE;
		} else if (this.isAttacking() && ticks <= 60) {
			event.getController().markNeedsReload();
			event.getController().setAnimation(
				new AnimationBuilder().addAnimation("animation.Statobot.attack", false)
			);
			return PlayState.CONTINUE;
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers (AnimationData animationData) {
		animationData.addAnimationController(
			new AnimationController<>(this, "controller", 0, this::predicate)
		);
	}

	@Override
	public AnimationFactory getFactory () {
		return this.animationFactory;
	}
}
