package net.shadowking21.shadowconfig.fabric;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.fabricmc.api.ModInitializer;

public final class ShadowConfigFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ShadowConfig.initPlatform(new SCFabricPlatformHook());
    }
}
