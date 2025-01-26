package ru.endlessrise.common.init

import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.endlessrise.EndlessRiseMain

object ModItems {

    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndlessRiseMain.MODID)

    //val ENDLESS_RISE = ITEMS.register("endless_rise")
}