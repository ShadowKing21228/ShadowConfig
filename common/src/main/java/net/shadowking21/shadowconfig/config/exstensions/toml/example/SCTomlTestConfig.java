package net.shadowking21.shadowconfig.config.exstensions.toml.example;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.toml.SCTomlConfig;
import net.shadowking21.shadowconfig.config.models.RandomConfig;

public class SCTomlTestConfig {
    public static SCTomlConfig<RandomConfig> commonRandomConfig;

    public static SCTomlConfig<RandomConfig> serverRandomConfig;

    public static SCTomlConfig<RandomConfig> clientRandomConfig;

    public static void init() {

        commonRandomConfig = (SCTomlConfig<RandomConfig>)
                SCTomlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.COMMON)
                        .build();

        serverRandomConfig = (SCTomlConfig<RandomConfig>)
                SCTomlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.SERVER)
                        .build();

        clientRandomConfig = (SCTomlConfig<RandomConfig>)
                SCTomlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.CLIENT)
                        .build();

        var var = clientRandomConfig.getCurrentConfig().randomValue;
        ShadowConfig.LOGGER.info(var);
        System.out.println(var);
    }
}
