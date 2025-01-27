package ru.endlessrise.common.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import ru.endlessrise.EndlessRiseMain

object ModBlocks {

    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EndlessRiseMain.MODID)

    val CHLOROPHYTE_ORE = BLOCKS.register("chlorophyte_ore") { Block(AbstractBlock.Properties.of(Material.STONE).strength(5f, 8f)) }

}