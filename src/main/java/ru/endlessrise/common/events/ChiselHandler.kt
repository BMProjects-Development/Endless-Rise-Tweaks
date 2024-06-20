package ru.endlessrise.common.events

import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.play.server.SAnimateBlockBreakPacket
import net.minecraft.network.play.server.SAnimateHandPacket
import net.minecraft.network.play.server.SPlaySoundEffectPacket
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import ru.endlessrise.common.recipes.ChiselRecipe
import ru.endlessrise.common.registry.ChiselRecipes
import ru.endlessrise.common.world.ChiselSaveData
import ru.endlessrise.common.world.ProgressData
import kotlin.random.Random

object ChiselHandler {

    @SubscribeEvent
    fun onBlockRightClicked(event: PlayerInteractEvent.RightClickBlock) {
        val player = event.player
        val level = player.level
        if (level.isClientSide) return // На клиенте проверять не нужно
        if (event.hand != Hand.MAIN_HAND) return // Проверяем только правую руку

        val progressData =
            (level as ServerWorld).dataStorage.computeIfAbsent(::ChiselSaveData, "chisel_save_data").progress

        val state = level.getBlockState(event.pos)

        ChiselRecipes.RECIPES
            .filter { it.input == state.block }
            .filter { it.check(player) }
            .forEach { recipe ->
                if (player.cooldowns.isOnCooldown(recipe.mainHandItem.item)) return
                val offhandItem = recipe.offhandItem
                if (offhandItem != null && player.cooldowns.isOnCooldown(offhandItem.item)) return
                event.isCanceled = true // Отключаем действие самого блока при нажатии

                processRecipe(recipe, player, level, event.pos, progressData)
            }
    }

    private fun processRecipe(
        recipe: ChiselRecipe,
        player: PlayerEntity,
        level: ServerWorld,
        pos: BlockPos,
        progressData: HashMap<BlockPos, ProgressData>,
    ) {
        val mainHandItem = player.getItemInHand(Hand.MAIN_HAND)
        val offHandItem = player.getItemInHand(Hand.OFF_HAND)

        val progress = progressData.computeIfAbsent(pos) { ProgressData() }
        val x = pos.x.toDouble()
        val y = pos.y.toDouble()
        val z = pos.z.toDouble()

        val randHand = Random.nextBoolean()

        for (playerEntity in level.server.playerList.players) {
            if (playerEntity.distanceToSqr(x, y, z) < 5000) {
                playerEntity.connection.send(
                    SPlaySoundEffectPacket(SoundEvents.ANVIL_HIT, SoundCategory.BLOCKS, x, y, z, 1f, 1f)
                )
            }
            playerEntity.connection.send(
                SAnimateBlockBreakPacket(
                    player.id, pos,
                    ((progress.breakCount / recipe.progress.toFloat()) * 9).toInt()
                )
            )
            playerEntity.connection.send(
                SAnimateHandPacket(player, if (randHand) 0 else 3)
            )
        }

        if (randHand) player.cooldowns.addCooldown(mainHandItem.item, 10)
        else player.cooldowns.addCooldown(offHandItem.item, 10)

        level.sendParticles(ParticleTypes.POOF, x + 0.5f, y + 0.5f, z + 0.5f, 3, 0.01, 0.01, 0.01, 0.0)

        if (mainHandItem.isDamageableItem) mainHandItem.hurtAndBreak(recipe.damageToItem, player) {}
        if (offHandItem.isDamageableItem) offHandItem.hurtAndBreak(recipe.damageToItem, player) {}

        if (progress.breakCount++ > recipe.progress) {
            level.destroyBlock(pos, false) // false - отключает дроп с блока

            val itemEntity = ItemEntity(
                level,
                x, y, z,
                recipe.output.copy()
            )

            level.sendParticles(ParticleTypes.POOF, x + 0.5f, y + 0.5f, z + 0.5f, 10, 0.1, 0.1, 0.1, 0.0)
            level.addFreshEntity(itemEntity)

            progressData.remove(pos)

            if (recipe.consumeItem) {
                mainHandItem.shrink(recipe.mainHandItem.count)
                offHandItem.shrink(recipe.offhandItem?.count ?: 0)
            }
        }
    }
}