package com.arkonix.chaosenchants;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChaosEvents {
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (player.tickCount % 10 != 0) return;

        for (ItemStack stack : player.getInventory().items) ChaosEnchantments.enchantIfNeeded(stack, player);
        for (ItemStack stack : player.getInventory().armor) ChaosEnchantments.enchantIfNeeded(stack, player);
        for (ItemStack stack : player.getInventory().offhand) ChaosEnchantments.enchantIfNeeded(stack, player);
    }

    @SubscribeEvent
    public void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof ServerPlayer player)) return;
        ChaosEnchantments.reroll(event.getCrafting(), player, false);
    }

    @SubscribeEvent
    public void onSmelt(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof ServerPlayer player)) return;
        ChaosEnchantments.reroll(event.getSmelting(), player, false);
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof ServerPlayer player)) return;
        ChaosEnchantments.enchantIfNeeded(event.getStack(), player);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.hasTag() || !stack.getTag().contains(ChaosEnchantments.TAG_DISPLAY_LEVELS)) return;
        CompoundTag levels = stack.getTag().getCompound(ChaosEnchantments.TAG_DISPLAY_LEVELS);
        if (levels.isEmpty()) return;
        event.getToolTip().add(Component.literal("Chaos Display Levels:").withStyle(ChatFormatting.DARK_PURPLE));
        int shown = 0;
        for (String key : levels.getAllKeys()) {
            if (shown >= 8) {
                event.getToolTip().add(Component.literal("...and more").withStyle(ChatFormatting.GRAY));
                break;
            }
            int level = levels.getInt(key);
            if (level > ChaosEnchantments.MAX_ACTUAL_LEVEL) {
                event.getToolTip().add(Component.literal(" • " + key + " " + level + " (God Display)").withStyle(ChatFormatting.GOLD));
            }
            shown++;
        }
    }
}
