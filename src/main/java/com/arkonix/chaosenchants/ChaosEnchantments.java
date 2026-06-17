package com.arkonix.chaosenchants;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ChaosEnchantments {
    public static final String TAG_GENERATED = "ChaosEnchantsGenerated";
    public static final String TAG_DISPLAY_LEVELS = "ChaosDisplayLevels";
    private static final Random RANDOM = new Random();

    // 1.20.1 stores enchantment levels as shorts. Actual working level is capped safely.
    public static final int MAX_ACTUAL_LEVEL = 32767;
    public static final int GOD_DISPLAY_LEVEL = 999_999_999;

    public static boolean shouldSkip(ItemStack stack) {
        return stack.isEmpty() || stack.is(Items.AIR) || stack.is(ModItems.CHAOS_CRYSTAL.get());
    }

    public static boolean hasGenerated(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(TAG_GENERATED);
    }

    public static void enchantIfNeeded(ItemStack stack, ServerPlayer player) {
        if (shouldSkip(stack) || hasGenerated(stack)) return;
        applyRandom(stack, player, false);
    }

    public static void reroll(ItemStack stack, ServerPlayer player, boolean forced) {
        if (shouldSkip(stack)) return;
        stack.removeTagKey("Enchantments");
        stack.removeTagKey("StoredEnchantments");
        stack.removeTagKey(TAG_DISPLAY_LEVELS);
        applyRandom(stack, player, forced);
    }

    private static void applyRandom(ItemStack stack, ServerPlayer player, boolean forced) {
        List<Enchantment> enchantments = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
        if (enchantments.isEmpty()) return;
        Collections.shuffle(enchantments, RANDOM);

        int amount = rollEnchantAmount();
        CompoundTag displayLevels = new CompoundTag();
        boolean godRoll = false;

        for (int i = 0; i < Math.min(amount, enchantments.size()); i++) {
            Enchantment enchantment = enchantments.get(i);
            LevelRoll roll = rollLevel();
            int actualLevel = Math.min(roll.displayLevel, MAX_ACTUAL_LEVEL);
            stack.enchant(enchantment, actualLevel);
            displayLevels.putInt(Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString(), roll.displayLevel);
            if (roll.god) godRoll = true;
        }

        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(TAG_GENERATED, true);
        tag.put(TAG_DISPLAY_LEVELS, displayLevels);

        if (godRoll && player != null) {
            celebrateGodRoll(player, stack);
        } else if (forced && player != null) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.2F);
        }
    }

    private static int rollEnchantAmount() {
        int r = RANDOM.nextInt(1000);
        if (r < 500) return 1;
        if (r < 750) return 2;
        if (r < 900) return 3;
        if (r < 970) return 4;
        if (r < 990) return 5;
        if (r < 999) return 6 + RANDOM.nextInt(5);      // 6-10
        return 15 + RANDOM.nextInt(16);                 // 15-30 chaos item
    }

    private static LevelRoll rollLevel() {
        int r = RANDOM.nextInt(10000);
        if (r < 4500) return new LevelRoll(1 + RANDOM.nextInt(5), false);                 // 45%
        if (r < 7500) return new LevelRoll(6 + RANDOM.nextInt(15), false);                // 30%
        if (r < 9000) return new LevelRoll(21 + RANDOM.nextInt(80), false);               // 15%
        if (r < 9900) return new LevelRoll(101 + RANDOM.nextInt(9900), false);            // 9%
        return new LevelRoll(10_000 + RANDOM.nextInt(GOD_DISPLAY_LEVEL - 10_000), true);  // 1%
    }

    private static void celebrateGodRoll(ServerPlayer player, ItemStack stack) {
        player.displayClientMessage(Component.literal("✦ GOD ROLL! ✦ ")
                .append(stack.getHoverName())
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD), false);
        player.level().playSound(null, player.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.level().playSound(null, player.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 0.6F, 1.5F);

        for (int i = 0; i < 3; i++) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(player.level());
            if (bolt != null) {
                double ox = (RANDOM.nextDouble() - 0.5) * 8.0;
                double oz = (RANDOM.nextDouble() - 0.5) * 8.0;
                bolt.moveTo(player.getX() + ox, player.getY(), player.getZ() + oz);
                bolt.setVisualOnly(true);
                player.level().addFreshEntity(bolt);
            }
        }
    }

    private record LevelRoll(int displayLevel, boolean god) {}
}
