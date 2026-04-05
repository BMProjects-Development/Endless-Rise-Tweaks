package ru.endlessrise.common.init

import net.minecraft.item.Item
import net.minecraft.item.Rarity
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.endlessrise.EndlessRiseMain

object ModItems {

    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndlessRiseMain.MODID)

    val ENDLESS_RISE = ITEMS.register("endless_rise") { Item(Item.Properties().tab(ModCreativeTabs.MAIN).stacksTo(1).rarity(Rarity.EPIC)) }
    val WET_PAPER = ITEMS.register("wet_paper") { Item(Item.Properties().tab(ModCreativeTabs.MAIN)) }
    val LAMP = ITEMS.register("lamp") { Item(Item.Properties().tab(ModCreativeTabs.MAIN)) }
    val PHOTOCELL = ITEMS.register("photocell") { Item(Item.Properties().tab(ModCreativeTabs.MAIN)) }
    val LENS = ITEMS.register("lens") { Item(Item.Properties().tab(ModCreativeTabs.MAIN)) }

}
