package com.arkonix.chaosenchants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChaosCrystalItem extends Item {
    public ChaosCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack crystal = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(crystal);

        ItemStack target = player.getOffhandItem();
        if (target.isEmpty() || target.is(this)) {
            player.displayClientMessage(Component.literal("Put the item you want to reroll in your offhand.").withStyle(ChatFormatting.RED), true);
            return InteractionResultHolder.fail(crystal);
        }

        ChaosEnchantments.reroll(target, (ServerPlayer) player, true);
        if (!player.getAbilities().instabuild) crystal.shrink(1);
        player.displayClientMessage(Component.literal("Chaos rerolled your offhand item!").withStyle(ChatFormatting.LIGHT_PURPLE), true);
        return InteractionResultHolder.success(crystal);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to reroll your offhand item.").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Every enchant. Every item. No rules.").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
