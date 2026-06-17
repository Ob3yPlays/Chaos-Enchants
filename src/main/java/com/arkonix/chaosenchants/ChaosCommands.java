package com.arkonix.chaosenchants;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.Collection;

public class ChaosCommands {
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("chaosenchants")
                .then(Commands.literal("info")
                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(() -> Component.literal("Chaos Enchants: every inventory item gets random impossible enchantments. God Tier chance: 1% per enchant.").withStyle(ChatFormatting.LIGHT_PURPLE), false);
                            return 1;
                        }))
                .then(Commands.literal("reroll")
                        .then(Commands.literal("hand")
                                .executes(ctx -> rerollHand(ctx.getSource().getPlayerOrException())))
                        .then(Commands.literal("offhand")
                                .executes(ctx -> rerollOffhand(ctx.getSource().getPlayerOrException())))
                        .then(Commands.literal("inventory")
                                .requires(source -> source.hasPermission(2))
                                .executes(ctx -> rerollInventory(ctx.getSource().getPlayerOrException()))))
                .then(Commands.literal("givecrystal")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("players", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64))
                                        .executes(ctx -> giveCrystals(EntityArgument.getPlayers(ctx, "players"), IntegerArgumentType.getInteger(ctx, "amount"))))))
        );

        event.getDispatcher().register(Commands.literal("ce")
                .redirect(event.getDispatcher().getRoot().getChild("chaosenchants")));
    }

    private static int rerollHand(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            player.sendSystemMessage(Component.literal("Hold an item first.").withStyle(ChatFormatting.RED));
            return 0;
        }
        ChaosEnchantments.reroll(stack, player, true);
        player.sendSystemMessage(Component.literal("Rerolled your main hand item.").withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int rerollOffhand(ServerPlayer player) {
        ItemStack stack = player.getOffhandItem();
        if (stack.isEmpty()) {
            player.sendSystemMessage(Component.literal("Put an item in your offhand first.").withStyle(ChatFormatting.RED));
            return 0;
        }
        ChaosEnchantments.reroll(stack, player, true);
        player.sendSystemMessage(Component.literal("Rerolled your offhand item.").withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int rerollInventory(ServerPlayer player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (!ChaosEnchantments.shouldSkip(stack)) {
                ChaosEnchantments.reroll(stack, player, false);
                count++;
            }
        }
        player.sendSystemMessage(Component.literal("Rerolled " + count + " inventory items.").withStyle(ChatFormatting.GREEN));
        return count;
    }

    private static int giveCrystals(Collection<ServerPlayer> players, int amount) {
        for (ServerPlayer player : players) {
            player.getInventory().add(new ItemStack(ModItems.CHAOS_CRYSTAL.get(), amount));
            player.sendSystemMessage(Component.literal("You received " + amount + " Chaos Crystal(s).").withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        return players.size();
    }
}
