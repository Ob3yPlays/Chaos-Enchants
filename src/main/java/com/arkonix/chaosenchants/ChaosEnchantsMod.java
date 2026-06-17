package com.arkonix.chaosenchants;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ChaosEnchantsMod.MOD_ID)
public class ChaosEnchantsMod {
    public static final String MOD_ID = "chaosenchants";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChaosEnchantsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreativeItems);

        MinecraftForge.EVENT_BUS.register(new ChaosEvents());
        MinecraftForge.EVENT_BUS.addListener(ChaosCommands::registerCommands);
    }

    private void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.CHAOS_CRYSTAL.get());
        }
    }
}
