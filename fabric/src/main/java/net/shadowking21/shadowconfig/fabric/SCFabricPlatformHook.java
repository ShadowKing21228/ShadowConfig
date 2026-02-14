package net.shadowking21.shadowconfig.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.shadowking21.shadowconfig.SCPlatformHook;
import net.shadowking21.shadowconfig.config.ConfigSide;

import java.nio.file.Path;

final class SCFabricPlatformHook implements SCPlatformHook {

    @Override
    public ConfigSide getCurrentSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER ? ConfigSide.SERVER : ConfigSide.CLIENT;
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean isDeveloper() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
