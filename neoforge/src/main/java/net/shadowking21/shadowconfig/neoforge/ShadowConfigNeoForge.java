package net.shadowking21.shadowconfig.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.shadowking21.shadowconfig.ShadowConfig;
import net.neoforged.fml.common.Mod;
import net.shadowking21.shadowconfig.config.ConfigSide;

@Mod(ShadowConfig.MOD_ID)
public final class ShadowConfigNeoForge {
    public ShadowConfigNeoForge() {
        // Run our common setup.
        ShadowConfig.init(FMLPaths.CONFIGDIR.get(), isClient() ? ConfigSide.CLIENT : ConfigSide.SERVER);
    }

    public static boolean isClient()
    {
        return FMLEnvironment.dist == Dist.CLIENT;
    }
}
