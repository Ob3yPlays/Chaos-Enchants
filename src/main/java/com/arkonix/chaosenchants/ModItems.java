package com.arkonix.chaosenchants;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChaosEnchantsMod.MOD_ID);

    public static final RegistryObject<Item> CHAOS_CRYSTAL = ITEMS.register("chaos_crystal",
            () -> new ChaosCrystalItem(new Item.Properties().stacksTo(64)));
}
