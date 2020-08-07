package motherlode.core.block;

import motherlode.core.block.defaults.DefaultShapedBlock;
import motherlode.core.block.entity.RedstoneTransmitterBlockEntity;
import motherlode.core.util.ShapeUtilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RedstoneTransmitterBlock extends DefaultShapedBlock implements BlockEntityProvider {
    public static final Box REDSTONE_TRANSMITTER_TORCH_SHAPE = new Box(0, 0.5, 0, 2F / 16F, 1, 2F / 16F);
    public static final VoxelShape REDSTONE_TRANSMITTER_SHAPE = VoxelShapes.union(VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1), VoxelShapes.cuboid(REDSTONE_TRANSMITTER_TORCH_SHAPE), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.EAST), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.SOUTH), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.WEST));

    public RedstoneTransmitterBlock() {
        super(REDSTONE_TRANSMITTER_SHAPE, AbstractBlock.Settings.of(Material.METAL).requiresTool().strength(3.0F, 3.0F));
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.isSneaking()) {
            if (!world.isClient()) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new RedstoneTransmitterBlockEntity();
    }
}
