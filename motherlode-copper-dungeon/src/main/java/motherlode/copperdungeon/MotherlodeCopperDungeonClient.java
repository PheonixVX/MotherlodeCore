package motherlode.copperdungeon;

import motherlode.copperdungeon.entity.renderers.StatoBotRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class MotherlodeCopperDungeonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(MotherlodeCopperDungeonBlocks.CUTTER_TRAP, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MotherlodeCopperDungeonBlocks.ZAPPER_TRAP, RenderLayer.getTranslucent());

        MotherlodeCopperDungeonParticles.init();

        // Initialize entity renderers
        EntityRendererRegistry.INSTANCE.register(
            MotherlodeCopperDungeon.STATO_BOT_ENTITY_TYPE,
            StatoBotRenderer::new
        );
    }
}
