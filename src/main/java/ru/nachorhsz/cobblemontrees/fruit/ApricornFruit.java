package ru.nachorhsz.cobblemontrees.fruit;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ApricornFruit extends Fruit {
    public static final TypedRegistry.EntryType<Fruit> TYPE
            = TypedRegistry.newType(ApricornFruit::new);

    public ApricornFruit(ResourceLocation registryName) {
        super(registryName);
    }

    protected FruitBlock createBlock(BlockBehaviour.Properties properties)
    {
        return new ApricornFruitBlock(properties, this);
    }

    @Override
    public void place(LevelAccessor level, BlockPos pos, @Nullable Float seasonValue)
    {
        BlockState state = getStateForAge(0);
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    @Override
    public void placeDuringWorldGen(LevelAccessor level, BlockPos pos, @Nullable Float seasonValue)
    {
        BlockState state = getStateForAge(getAgeForWorldGen(level, pos, seasonValue));
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }
}
