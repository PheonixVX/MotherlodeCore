package motherlode.copperdungeon.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

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
		if (ticks == 20 * 3) {
			// Get closest player and attack
			Box box = this.getBoundingBox().expand(3);
			PlayerEntity closestPlayer = this.world.getClosestPlayer(this, 3);
			if (closestPlayer != null && !this.world.isClient()) {
				if (!closestPlayer.isCreative()) {
					this.setTarget(closestPlayer);
					System.out.println("Attacking player");
				}
			}
			ticks = 0;
		}
	}

	/*
	 * Controls animations of the mob.
	 */
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (!(lastLimbDistance > -0.15F && lastLimbDistance < 0.15F) && !this.isAttacking()) {
			// Assume they are walking
			event.getController().setAnimation(
				new AnimationBuilder().addAnimation("walking", true)
			);
		} else if (this.getTarget() != null && ticks == 20 * 3) {
			event.getController().setAnimation(
				new AnimationBuilder().addAnimation("attack", false)
			);
		}
		else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
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
