package net.shadowking21.shadowconfig.config.exstensions.jsonc.example;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.jsonc.SCJsoncConfig;
import net.shadowking21.shadowconfig.config.models.RandomConfig;

public class SCJsoncTestConfig {

    public static SCJsoncConfig<RandomConfig> commonRandomConfig;

    public static SCJsoncConfig<RandomConfig> serverRandomConfig;

    public static SCJsoncConfig<RandomConfig> clientRandomConfig;

    public static void init() {

        commonRandomConfig = (SCJsoncConfig<RandomConfig>)
                SCJsoncConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.COMMON)
                        .build();

        serverRandomConfig = (SCJsoncConfig<RandomConfig>)
                SCJsoncConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.SERVER)
                        .build();

        clientRandomConfig = (SCJsoncConfig<RandomConfig>)
                SCJsoncConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.CLIENT)
                        .build();

        var var = clientRandomConfig.getCurrentConfig().randomValue;
        ShadowConfig.LOGGER.info(var);
        System.out.println(var);
    }
}
