package ru.endlessrise.common.init

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.endlessrise.EndlessRiseMain
import ru.endlessrise.common.init.ModBlocks.CHLOROPHYTE_ORE

object ModItems {

    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EndlessRiseMain.MODID)

    val ENDLESS_RISE = ITEMS.register("endless_rise") { Item(Item.Properties().stacksTo(1)) }

    val CHLOROPHYTE_ORE_BLOCK_ITEMS = ITEMS.register("chlorophyte_ore") { BlockItem(CHLOROPHYTE_ORE.get(), Item.Properties()) }
}