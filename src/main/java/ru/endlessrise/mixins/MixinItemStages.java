package ru.endlessrise.mixins;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.itemstages.ItemStages;
import net.darkhax.itemstages.Restriction;
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStages.class, remap = false)
public abstract class MixinItemStages {

    @Unique
    private static final int ITEMSTAGES_SCAN_INTERVAL = 10;

    @Inject(method = "onPlayerTick", at = @At("HEAD"), cancellable = true)
    private void itemstages$optimizedPlayerTick(TickEvent.PlayerTickEvent event, CallbackInfo ci) {
        ci.cancel();

        if (event.phase != Phase.START) {
            return;
        }

        final PlayerEntity player = event.player;
        if (player == null || player.level.isClientSide || player instanceof FakePlayer) {
            return;
        }

        if ((player.tickCount % ITEMSTAGES_SCAN_INTERVAL) != 0) {
            return;
        }

        final IStageData stageData = GameStageHelper.getPlayerData(player);
        final PlayerInventory inv = player.inventory;

        final int armorStart = inv.items.size();
        final int armorEndExclusive = armorStart + inv.armor.size();
        final int containerSize = inv.getContainerSize();

        for (int slot = 0; slot < containerSize; slot++) {
            final ItemStack stack = inv.getItem(slot);

            if (stack.isEmpty()) {
                continue;
            }

            final boolean armorSlot = slot >= armorStart && slot < armorEndExclusive;

            final Restriction restriction = armorSlot
                    ? RestrictionManager.INSTANCE.getEquipmentRestriction(player, stageData, stack)
                    : RestrictionManager.INSTANCE.getInventoryRestriction(player, stageData, stack);

            if (restriction == null) {
                continue;
            }

            if (armorSlot) {
                if (!restriction.shouldPreventEquipment()) {
                    continue;
                }
            } else {
                if (!restriction.shouldPreventInventory()) {
                    continue;
                }
            }

            inv.setItem(slot, ItemStack.EMPTY);
            player.drop(stack, false);

            final ITextComponent message = restriction.getDropMessage(stack);
            if (message != null) {
                player.sendMessage(message, Util.NIL_UUID);
            }
        }
    }
}