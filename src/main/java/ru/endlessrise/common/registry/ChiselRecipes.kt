package ru.endlessrise.common.registry

import com.alcatrazescapee.notreepunching.common.items.ModItems
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
            ItemStack(Items.STRING, 4),
            10,
            offhandItem = ItemStack(Items.STICK, 4)
        )
    )
}