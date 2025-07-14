package ru.nachorhsz.cobblemontrees.init;


import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import ru.nachorhsz.cobblemontrees.CobblemonTreesMod;
import ru.nachorhsz.cobblemontrees.fruit.ApricornFruit;


@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CobblemonTreesFruits {

    @SubscribeEvent
    public static void registerFruitTypes(final TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class))
        {
            event.registerType(CobblemonTreesMod.location("apricorn"), ApricornFruit.TYPE);
        }
    }
}
