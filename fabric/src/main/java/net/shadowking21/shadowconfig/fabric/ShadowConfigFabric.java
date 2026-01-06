package net.shadowking21.shadowconfig.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.shadowking21.shadowconfig.ShadowConfig;
import net.fabricmc.api.ModInitializer;
import net.shadowking21.shadowconfig.config.ConfigSide;

public final class ShadowConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ShadowConfig.init(FabricLoader.getInstance().getConfigDir(), isClient() ? ConfigSide.CLIENT : ConfigSide.SERVER);
    }

    public static boolean isClient()
    {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
