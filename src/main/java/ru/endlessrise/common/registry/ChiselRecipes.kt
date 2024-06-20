package ru.endlessrise.common.registry

import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import ru.endlessrise.common.recipes.ChiselRecipe

object ChiselRecipes {
    val RECIPES = arrayListOf(
        ChiselRecipe(Blocks.GOLD_BLOCK, ItemStack(Items.DIAMOND_BLOCK), Items.DIAMOND_SWORD, 10)
    )
}