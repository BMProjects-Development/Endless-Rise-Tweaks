package ru.endlessrise.mixins;

import com.google.common.collect.Multimap;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.itemstages.Restriction;
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = RestrictionManager.class, remap = false)
public abstract class MixinRestrictionManager {

    @Shadow @Final
    private Multimap<String, Restriction> restrictions;

    @Shadow @Final
    private Multimap<String, Restriction> preventInventory;

    @Shadow @Final
    private Multimap<String, Restriction> preventEquipment;

    @Shadow
    private boolean hasBuiltCaches;

    @Unique
    private void itemstages$ensureCachesBuilt() {
        if (this.hasBuiltCaches) {
            return;
        }

        this.preventInventory.clear();
        this.preventEquipment.clear();

        for (final String stage : this.restrictions.keySet()) {
            for (final Restriction restriction : this.restrictions.get(stage)) {
                if (restriction.shouldPreventInventory()) {
                    this.preventInventory.put(stage, restriction);
                }

                if (restriction.shouldPreventEquipment()) {
                    this.preventEquipment.put(stage, restriction);
                }
            }
        }

        this.hasBuiltCaches = true;
    }

    @Inject(
            method = "getRestriction(Lnet/minecraft/entity/player/PlayerEntity;Lnet/darkhax/gamestages/data/IStageData;Lnet/minecraft/item/ItemStack;Lcom/google/common/collect/Multimap;)Lnet/darkhax/itemstages/Restriction;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void itemstages$fastRestrictionLookup(
            PlayerEntity player,
            IStageData stageData,
            ItemStack stack,
            Multimap<String, Restriction> restrictionPool,
            CallbackInfoReturnable<Restriction> cir
    ) {
        if (stack.isEmpty()) {
            cir.setReturnValue(null);
            return;
        }

        this.itemstages$ensureCachesBuilt();

        final Set<Restriction> checked = new HashSet<>();

        for (final String stageName : restrictionPool.keySet()) {
            if (GameStageHelper.hasStage(player, stageData, stageName)) {
                continue;
            }

            for (final Restriction restriction : restrictionPool.get(stageName)) {
                if (!checked.add(restriction)) {
                    continue;
                }

                if (restriction.isRestricted(stack) && !restriction.meetsRequirements(player, stageData)) {
                    cir.setReturnValue(restriction);
                    return;
                }
            }
        }

        cir.setReturnValue(null);
    }

    @Inject(method = "apply", at = @At("TAIL"))
    private void itemstages$clearEquipmentCache(Void object, IResourceManager resourceManager, IProfiler profiler, CallbackInfo ci) {
        this.preventEquipment.clear();
    }
}