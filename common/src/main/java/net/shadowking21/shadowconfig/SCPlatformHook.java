package net.shadowking21.shadowconfig;

import net.shadowking21.shadowconfig.config.ConfigSide;

import java.nio.file.Path;

public interface SCPlatformHook {

    ConfigSide getCurrentSide();

    Path getConfigPath();

    boolean isDeveloper();
}
