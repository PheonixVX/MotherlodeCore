package motherlode.redstone.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import motherlode.base.util.ShapeUtilities;
import motherlode.redstone.MotherlodeRedstoneBlockEntities;

public class RedstoneTransmitterBlock extends BlockWithEntity {
    public static final Box REDSTONE_TRANSMITTER_TORCH_SHAPE = new Box(0, 0.5, 0, 2F / 16F, 1, 2F / 16F);
    public static final VoxelShape REDSTONE_TRANSMITTER_SHAPE = VoxelShapes.union(VoxelShapes.cuboid(0, 0, 0, 1, 0.5, 1), VoxelShapes.cuboid(REDSTONE_TRANSMITTER_TORCH_SHAPE), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.EAST), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.SOUTH), ShapeUtilities.getRotatedShape(REDSTONE_TRANSMITTER_TORCH_SHAPE, Direction.WEST));

    public final VoxelShape shape;

    public static final IntProperty POWER = Properties.POWER;
    public static final BooleanProperty RECEIVER = BooleanProperty.of("receiver");

    public RedstoneTransmitterBlock(Settings settings) {
        super(settings);
        this.shape = REDSTONE_TRANSMITTER_SHAPE;

        this.setDefaultState(this.getDefaultState().with(POWER, 0).with(RECEIVER, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
    }

    @Override
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(RECEIVER) ? state.get(POWER) : 0;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneTransmitterBlockEntity(pos, state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return !world.isClient ? BlockWithEntity.checkType(type, MotherlodeRedstoneBlockEntities.REDSTONE_TRANSMITTER, RedstoneTransmitterBlockEntity::tick) : null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWER).add(RECEIVER);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.getBlockTickScheduler().schedule(pos, this, 4);
        if (world.isClient())
            return;
        ((RedstoneTransmitterBlockEntity) world.getBlockEntity(pos)).register();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof RedstoneTransmitterBlockEntity) {
                ((RedstoneTransmitterBlockEntity) be).update();
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof RedstoneTransmitterBlockEntity) {
            if (!world.isClient())
                ((RedstoneTransmitterBlockEntity) be).remove();
            ItemScatterer.spawn(world, pos, (Inventory) be);
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof RedstoneTransmitterBlockEntity)
                ((RedstoneTransmitterBlockEntity) be).update();
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
