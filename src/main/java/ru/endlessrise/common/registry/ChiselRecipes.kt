package ru.endlessrise.common.registry

import com.teammetallurgy.atum.init.AtumBlocks
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import ru.endlessrise.common.recipes.ChiselRecipe

object ChiselRecipes {
    val RECIPES = arrayListOf(
        ChiselRecipe(
            Blocks.SANDSTONE,
            ItemStack(AtumBlocks.QUERN),
            ItemStack(Items.STICK, 4),
            10,
            offhandItem = ItemStack(Items.FLINT, 3)
        )
    )
}