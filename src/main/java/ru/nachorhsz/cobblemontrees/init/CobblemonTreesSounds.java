package ru.nachorhsz.cobblemontrees.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.nachorhsz.cobblemontrees.CobblemonTreesMod;

public class CobblemonTreesSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, CobblemonTreesMod.MODID);
    public static final DeferredHolder<SoundEvent, SoundEvent> APRICORN_FELL = REGISTRY.register("apricorn_fall", () ->
            SoundEvent.createVariableRangeEvent(CobblemonTreesMod.location("apricorn_fall")));
}
