package ru.endlessrise.mixins;

import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockHarvestAccessor {
    @Accessor("harvestTool")
    @Mutable
    void endlessrise$setHarvestTool(ToolType harvestTool);

    @Accessor("harvestLevel")
    @Mutable
    void endlessrise$setHarvestLevel(int harvestLevel);
}
