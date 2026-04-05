package ru.endlessrise.mixins;

import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuernBlock.class)
public abstract class QuernBlockMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void endlessrise$allowHandHarvest(CallbackInfo ci) {
        BlockHarvestAccessor accessor = (BlockHarvestAccessor) this;
        accessor.endlessrise$setHarvestTool(null);
        accessor.endlessrise$setHarvestLevel(-1);
    }
}
