package motherlode.copperdungeon.entity.models;

import motherlode.copperdungeon.MotherlodeModule;
import motherlode.copperdungeon.entity.StatoBotEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class StatoBotModel extends AnimatedGeoModel<StatoBotEntity> {

	@Override
	public Identifier getModelLocation (StatoBotEntity object) {
		return new Identifier(MotherlodeModule.MODID, "geo/statobot.geo.json");
	}

	@Override
	public Identifier getTextureLocation (StatoBotEntity object) {
		return new Identifier(MotherlodeModule.MODID, "textures/entity/statobot.png");
	}

	@Override
	public Identifier getAnimationFileLocation (StatoBotEntity animatable) {
		return new Identifier(MotherlodeModule.MODID, "animations/statobot.animation.json");
	}
}
