package ru.nachorhsz.cobblemontrees.mixin;

import com.cobblemon.mod.neoforge.worldgen.CobblemonBiomeModifiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CobblemonBiomeModifiers.class)
public class CobblemonTreeCanceler {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelTreeGen(@NotNull ResourceKey<PlacedFeature> feature, @NotNull GenerationStep.Decoration step, @Nullable TagKey<Biome> validTag, CallbackInfo ci) {
        if(feature.isFor(Registries.PLACED_FEATURE) && GenerationStep.Decoration.VEGETAL_DECORATION.equals(step)) {
            if(feature.location().getPath().contains("tree")) {
                ci.cancel();
            }
        }
    }
}