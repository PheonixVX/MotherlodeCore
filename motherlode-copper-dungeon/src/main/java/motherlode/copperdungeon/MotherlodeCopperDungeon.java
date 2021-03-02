package motherlode.copperdungeon;

import motherlode.copperdungeon.entity.StatoBotEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MotherlodeCopperDungeon implements ModInitializer {

	public static final EntityType<StatoBotEntity> STATO_BOT_ENTITY_TYPE = Registry.register(
		Registry.ENTITY_TYPE,
		new Identifier(MotherlodeModule.MODID, "statobot"),
		FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StatoBotEntity::new)
			.dimensions(EntityDimensions.fixed(0.75f, 0.75f))
			.build()
	);

	@Override
	public void onInitialize () {
		FabricDefaultAttributeRegistry.register(
			STATO_BOT_ENTITY_TYPE,
			StatoBotEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
		);
	}
}
