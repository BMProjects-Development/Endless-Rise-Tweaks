package ru.endlessrise.common.init

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import com.teammetallurgy.atum.init.AtumBlocks

@JeiPlugin
class JEIPlugins : IModPlugin {

    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation("endlessrise", "jei_descriptions")
    }

    override fun registerRecipes(registration: IRecipeRegistration) {

        val quernStack = ItemStack(AtumBlocks.QUERN)

        registration.addIngredientInfo(
            quernStack,
            VanillaTypes.ITEM,
            TranslationTextComponent("jei.description.quern_how_to_create")
        )
    }
}