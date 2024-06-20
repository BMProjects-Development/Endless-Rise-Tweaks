package ru.endlessrise.common.world

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.storage.WorldSavedData

class ChiselSaveData : WorldSavedData("chisel_save_data") {
    val progress = HashMap<BlockPos, ProgressData>()

    override fun load(tag: CompoundNBT) {
        progress.clear()

        // 10 - id для CompountNBT
        tag.getList("progress", 10).forEach {
            val nbt = it as CompoundNBT

            val x = nbt.getInt("x")
            val y = nbt.getInt("y")
            val z = nbt.getInt("z")
            progress[BlockPos(x, y, z)] = ProgressData(nbt.getInt("progress"))
        }
    }

    override fun save(tag: CompoundNBT): CompoundNBT {
        tag.put("progress", ListNBT().apply {
            addAll(progress.map {
                CompoundNBT().apply {
                    putInt("x", it.key.x)
                    putInt("y", it.key.y)
                    putInt("z", it.key.z)
                    putInt("progress", it.value.breakCount)
                }
            })
        })
        return tag
    }

    override fun isDirty(): Boolean {
        return true
    }
}

class ProgressData(var breakCount: Int = 0)