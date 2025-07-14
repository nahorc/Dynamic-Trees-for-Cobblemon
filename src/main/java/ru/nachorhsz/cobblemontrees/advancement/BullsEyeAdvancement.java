package ru.nachorhsz.cobblemontrees.advancement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

public class BullsEyeAdvancement {

    public static void onCheckInApple(ProjectileImpactEvent event, Entity entity) {

        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        GameType mode = player.gameMode.getGameModeForPlayer();

        if (mode != GameType.SURVIVAL) {
            return;
        }

        var advancements = player.server.getAdvancements();
        var advancement = advancements.get(ResourceLocation.parse("cobblemontrees:bulls_eye"));

        if (advancement == null) {
            return;
        }

        var progress = player.getAdvancements().getOrStartProgress(advancement);

        if (progress.isDone()) {
            return;
        }

        for (String criterion : progress.getRemainingCriteria()) {
            player.getAdvancements().award(advancement, criterion);
        }
    }
}
