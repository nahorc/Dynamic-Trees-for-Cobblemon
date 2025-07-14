package ru.nachorhsz.cobblemontrees;

import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import ru.nachorhsz.cobblemontrees.init.CobblemonTreesSounds;

@Mod(ru.nachorhsz.cobblemontrees.CobblemonTreesMod.MODID)
public class CobblemonTreesMod {
    public static final String MODID = "cobblemontrees";

    public CobblemonTreesMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHandler.setup(MODID, modEventBus);

        CobblemonTreesSounds.REGISTRY.register(modEventBus);
    }

    public static ResourceLocation location(final String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
