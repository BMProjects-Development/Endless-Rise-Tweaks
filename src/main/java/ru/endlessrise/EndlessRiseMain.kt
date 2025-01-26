package ru.endlessrise

import net.minecraftforge.common.MinecraftForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import ru.endlessrise.common.events.ChiselHandler

class EndlessRiseMain {
    companion object {
        const val MODID = "endlessrise"
        val LOGGER: Logger = LogManager.getLogger()
    }

    init {
        LOGGER.info("Loading EndlessRise Mod!")

        MinecraftForge.EVENT_BUS.register(ChiselHandler)
    }
}