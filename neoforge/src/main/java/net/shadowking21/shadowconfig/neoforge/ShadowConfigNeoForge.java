package net.shadowking21.shadowconfig.neoforge;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.neoforged.fml.common.Mod;

@Mod(ShadowConfig.MOD_ID)
public final class ShadowConfigNeoForge {
    public ShadowConfigNeoForge() {
        ShadowConfig.initPlatform(new SCNeoForgePlatformHook());
    }

}
