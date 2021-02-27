package motherlode.copperdungeon.entity.renderers;

import motherlode.copperdungeon.MotherlodeModule;
import motherlode.copperdungeon.entity.StatoBotEntity;
import motherlode.copperdungeon.entity.models.StatoBotModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class StatoBotRenderer extends GeoEntityRenderer<StatoBotEntity> {
	public StatoBotRenderer (EntityRendererFactory.Context ctx) {
		super(ctx, new StatoBotModel());
	}

	@Override
	public Identifier getTexture (StatoBotEntity entity) {
		return new Identifier(MotherlodeModule.MODID, "textures/entity/statobot.png");
	}
}
