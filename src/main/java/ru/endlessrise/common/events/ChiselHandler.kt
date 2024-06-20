package ru.endlessrise.common.events

import net.minecraft.entity.item.ItemEntity
import net.minecraft.network.play.server.SAnimateBlockBreakPacket
import net.minecraft.network.play.server.SAnimateHandPacket
import net.minecraft.network.play.server.SPlaySoundEffectPacket
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import ru.endlessrise.common.registry.ChiselRecipes
import ru.endlessrise.common.world.ChiselSaveData
import ru.endlessrise.common.world.ProgressData

object ChiselHandler {

    @SubscribeEvent
    fun onBlockRightClicked(event: PlayerInteractEvent.RightClickBlock) {
        val player = event.player
        val level = player.level
        if (level.isClientSide) return // На клиенте проверять не нужно
        if (event.hand != Hand.MAIN_HAND) return // Проверяем только правую руку

        val progressData =
            (level as ServerWorld).dataStorage.computeIfAbsent(::ChiselSaveData, "chisel_save_data").progress

        val handItemStack = player.getItemInHand(event.hand)
        val state = level.getBlockState(event.pos)

        ChiselRecipes.RECIPES
            .filter { it.input == state.block }
            .filter { it.rightClickItem == handItemStack.item }
            .forEach { recipe ->
                event.isCanceled = true // Отключаем действие самого блока при нажатии

                val progress = progressData.computeIfAbsent(event.pos) { ProgressData() }
                val x = event.pos.x.toDouble()
                val y = event.pos.y.toDouble()
                val z = event.pos.z.toDouble()

                for (playerEntity in level.server.playerList.players) {
                    if (playerEntity.distanceToSqr(x, y, z) < 5000) {
                        playerEntity.connection.send(
                            SPlaySoundEffectPacket(SoundEvents.ANVIL_HIT, SoundCategory.BLOCKS, x, y, z, 1f, 1f)
                        )
                    }
                    playerEntity.connection.send(
                        SAnimateBlockBreakPacket(
                            event.player.id, event.pos,
                            ((progress.breakCount / recipe.progress.toFloat()) * 9).toInt()
                        )
                    )
                    playerEntity.connection.send(
                        SAnimateHandPacket(player, 0)
                    )
                }

                level.sendParticles(ParticleTypes.POOF, x + 0.5f, y + 0.5f, z + 0.5f, 3, 0.01, 0.01, 0.01, 0.0)

                if (handItemStack.isDamageableItem) {
                    handItemStack.damageValue += recipe.damageToItem
                }

                if (progress.breakCount++ > recipe.progress) {
                    level.destroyBlock(event.pos, false) // false - отключает дроп с блока

                    val itemEntity = ItemEntity(
                        level,
                        x, y, z,
                        recipe.output.copy()
                    )

                    level.sendParticles(ParticleTypes.POOF, x + 0.5f, y + 0.5f, z + 0.5f, 10, 0.1, 0.1, 0.1, 0.0)
                    level.addFreshEntity(itemEntity)

                    progressData.remove(event.pos)
                }
            }
    }
}