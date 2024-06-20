package ru.endlessrise.common.recipes

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ChiselRecipe(
    val input: Block,
    val output: ItemStack,
    val rightClickItem: Item,
    val progress: Int = 7,
    val damageToItem: Int = 5
)