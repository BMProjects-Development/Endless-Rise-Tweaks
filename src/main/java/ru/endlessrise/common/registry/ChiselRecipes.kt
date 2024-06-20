package ru.endlessrise.common.registry

import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import ru.endlessrise.common.recipes.ChiselRecipe

object ChiselRecipes {
    val RECIPES = arrayListOf(
        ChiselRecipe(
            Blocks.GOLD_BLOCK,
            ItemStack(Items.DIAMOND_BLOCK),
            ItemStack(Items.STICK, 5),
            10,
            offhandItem = ItemStack(Items.FLINT, 2)
        )
    )
}