package ru.endlessrise.common.init

import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object ModCreativeTabs {

    val MAIN: ItemGroup = object : ItemGroup("endlessrise") {
        override fun makeIcon(): ItemStack = ItemStack(ModItems.ENDLESS_RISE.get())
    }
}
