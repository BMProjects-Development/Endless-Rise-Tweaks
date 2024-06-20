package ru.endlessrise.common.recipes

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

data class ChiselRecipe(
    val input: Block,
    val output: ItemStack,
    val mainHandItem: ItemStack,
    val progress: Int = 7,
    val offhandItem: ItemStack? = null,
    val damageToItem: Int = 5,
    val consumeItem: Boolean = true,
) {
    fun check(player: PlayerEntity): Boolean {
        if (player.mainHandItem.item != mainHandItem.item) return false
        if(player.mainHandItem.count < mainHandItem.count) return false

        val offhandItem = offhandItem
        if (offhandItem != null) {
            if (player.offhandItem.item != offhandItem.item) return false
            if (player.offhandItem.count < offhandItem.count) return false
        }
        return true
    }
}