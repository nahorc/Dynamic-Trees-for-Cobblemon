package ru.nachorhsz.cobblemontrees.procedure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import ru.nachorhsz.cobblemontrees.advancement.BullsEyeAdvancement;
import ru.nachorhsz.cobblemontrees.fruit.ApricornFruitBlock;

import java.util.Objects;

@EventBusSubscriber
public class ProjectileApricorn
{
    static final double DISTANCE_THRESHOLD_SQR = 400;

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event)
    {
        if (event.getRayTraceResult() instanceof BlockHitResult hit)
        {
            Level level = event.getProjectile().level();
            BlockPos pos = hit.getBlockPos();
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof ApricornFruitBlock fruit)
            {
                fruit.doFallSimple(state, level, pos);

                if (event.getProjectile() instanceof Arrow arrow && arrow.getOwner() != null) {
                    handleArrowHit(level, arrow, pos, event);
                }
            }
        }
    }

    private static void handleArrowHit(Level level, Arrow arrow, BlockPos pos, ProjectileImpactEvent event)
    {
        Vec3 posVec = Vec3.atCenterOf(pos);
        double distance = Objects.requireNonNull(arrow.getOwner()).distanceToSqr(posVec);

        if (distance >= DISTANCE_THRESHOLD_SQR)
        {
            BullsEyeAdvancement.onCheckInApple(event, arrow.getOwner());
        }

        if (arrow.pickup == AbstractArrow.Pickup.ALLOWED)
        {
            ItemStack arrowItem = new ItemStack(Items.ARROW);
            ItemEntity itemEntity = new ItemEntity(level, arrow.getX(), arrow.getY(), arrow.getZ(), arrowItem);
            level.addFreshEntity(itemEntity);
        }
        arrow.discard();
    }
}
