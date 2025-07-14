package ru.nachorhsz.cobblemontrees.fruit;

import com.dtteam.dynamictrees.block.branch.BasicRootsBlock;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.tree.TreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class ApricornFruitBlock extends FruitBlock implements IFruitFallable
{
    public ApricornFruitBlock(Properties properties, Fruit fruit) {
        super(properties, fruit);
    }

    @Override
    public float getRandomFruitFallChance() {
        return 0.01f;
    }

    @Override
    public float getPlayerDistanceToFall() {
        return 24;
    }

    @Override
    public boolean isSupported(LevelReader world, BlockPos pos, BlockState state)
    {
        final BlockState branchState = world.getBlockState(pos.above());
        final BranchBlock branch = TreeHelper.getBranch(branchState);
        if (branch != null)
        {
            return branch.getRadius(branchState) <= 3;
        }
        return branchState.getBlock() instanceof DynamicLeavesBlock;
    }

    @Override
    public void doTick(BlockState state, Level world, BlockPos pos, RandomSource random)
    {
        if (checkToFall(state, world, pos, random))
        {
            doFall(state, world, pos);
        }
        else
        {
            super.doTick(state, world, pos, random);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (!isSupported(world, pos, state))
        {
            if (!doFall(state, world, pos))
            {
                super.neighborChanged(state, world, pos, block, fromPos, isMoving);
            }
        }
        DebugPackets.sendNeighborsUpdatePacket(world, pos);
    }

    public ItemStack getDropOnFallItems(@Nonnull FallingBlockEntity entity)
    {
        if (entity.getServer() == null)
        {
            return ItemStack.EMPTY;
        }
        ServerLevel level = entity.getServer().getLevel(entity.level().dimension());
        if (level == null)
        {
            return ItemStack.EMPTY;
        }
        List<ItemStack> drops = getDrops(entity.getBlockState(), level, entity.blockPosition(), null);
        if (drops.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        return drops.getFirst();
    }

    @Override
    public int getRootY(BlockState state, Level world, BlockPos pos)
    {
        for (int i = 0; i < 20; i++) {
            BlockPos pos2 = pos.below(i);
            if (world.getBlockState(pos2).getBlock() instanceof BasicRootsBlock) {
                return pos2.getY();
            }
        }
        return pos.getY();
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return fruit.getBlockShape(getAge(state));
    }
}