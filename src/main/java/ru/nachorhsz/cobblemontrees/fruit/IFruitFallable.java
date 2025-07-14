package ru.nachorhsz.cobblemontrees.fruit;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import ru.nachorhsz.cobblemontrees.init.CobblemonTreesSounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IFruitFallable
{
    ItemStack getDropOnFallItems(FallingBlockEntity entity);
    float getRandomFruitFallChance();
    float getPlayerDistanceToFall();
    int getAge(BlockState state);
    int getMaxAge();

    int fallDamageMax = 40;
    float fallDamageAmount = 2.0F;

    default boolean checkToFall(BlockState state, Level world, BlockPos pos, RandomSource random)
    {
        if (getAge(state) < getMaxAge()) return false;
        return random.nextFloat() <= getRandomFruitFallChance();
    }

    int getRootY(BlockState state, Level world, BlockPos pos);

    default FallingBlockEntity getFallingEntity(Level world, BlockPos pos, BlockState state) {
        return new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state) {
            @Override
            public void tick() {
                super.tick();
            }

            @Override
            public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
                int i = (int)Math.ceil(pFallDistance - 1.0F);
                if (i > 0) {
                    List<Entity> list = Lists.newArrayList(level().getEntities(this, this.getBoundingBox()));
                    for (Entity entity : list) {
                        if (entity instanceof LivingEntity) {
                            entity.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(
                                            Registries.DAMAGE_TYPE, ResourceLocation.parse("cobblemontrees:fall_apricorn_damage")))),
                                    (float)Math.min(Math.floor((float)i * fallDamageAmount), fallDamageMax) * pMultiplier);
                        }
                    }
                    level().playSound(null, pos,
                            CobblemonTreesSounds.APRICORN_FELL.get(), SoundSource.BLOCKS,
                            3.0F, 1.0F);
                }
                return false;
            }

            @Nullable
            @Override
            public ItemEntity spawnAtLocation(@Nonnull ItemStack pStack, float pOffsetY) {
                return super.spawnAtLocation(getDropOnFallItems(this), pOffsetY);
            }
        };
    }

    default boolean doFall(BlockState state, Level world, BlockPos pos)
    {
        if (getPlayerDistanceToFall() <= 0) return false;
        int rootY = getRootY(state, world, pos);
        if (!world.hasNearbyAlivePlayer(pos.getX(), rootY, pos.getZ(), getPlayerDistanceToFall()))
        {
            return false;
        }
        if (pos.getY() >= world.getMinBuildHeight() && FallingBlock.isFree(world.getBlockState(pos.below())))
        {
            if (world.isLoaded(pos))
            {
                if (!world.isClientSide())
                {
                    FallingBlockEntity fallingBlockEntity = getFallingEntity(world, pos, state);
                    world.addFreshEntity(fallingBlockEntity);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    return true;
                }
            }
        }
        return false;
    }

    default void doFallSimple(BlockState state, Level world, BlockPos pos) {
        if (!world.isLoaded(pos) || world.isClientSide()) return;

        if (FallingBlock.isFree(world.getBlockState(pos.below()))) {
            FallingBlockEntity fallingEntity = getFallingEntity(world, pos, state);
            world.addFreshEntity(fallingEntity);
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        } else {
            world.destroyBlock(pos, true);
        }
    }
}
