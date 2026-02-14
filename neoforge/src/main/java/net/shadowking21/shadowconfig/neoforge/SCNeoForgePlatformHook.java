package net.shadowking21.shadowconfig.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.shadowking21.shadowconfig.SCPlatformHook;
import net.shadowking21.shadowconfig.config.ConfigSide;

import java.nio.file.Path;

final class SCNeoForgePlatformHook implements SCPlatformHook {

    @Override
    public ConfigSide getCurrentSide() {
        return FMLEnvironment.dist == Dist.CLIENT ? ConfigSide.CLIENT : ConfigSide.SERVER;
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDeveloper() {
        return !FMLEnvironment.production;
    }
}
