package ru.endlessrise

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.event.lifecycle.IModBusEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.endlessrise.common.events.ChiselHandler
import ru.endlessrise.common.init.ModItems
import ru.endlessrise.common.world.ImmersiveEngineeringWorldTweaks

class EndlessRiseMain {
    companion object {
        const val MODID = "endlessrise"
        val LOGGER: Logger = LogManager.getLogger()
    }

    init {
        LOGGER.info("Loading EndlessRise Tweaks!")

        MinecraftForge.EVENT_BUS.register(ChiselHandler)
        val IModBusEvent = FMLJavaModLoadingContext.get().modEventBus
        ModItems.ITEMS.register(IModBusEvent)
        IModBusEvent.addListener(ImmersiveEngineeringWorldTweaks::onLoadComplete)
    }

}
