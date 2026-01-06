package net.shadowking21.shadowconfig.config.exstensions.json.example;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.json.SCJsonConfig;
import net.shadowking21.shadowconfig.config.models.RandomConfig;

public class SCJsonTestConfig {

    public static SCJsonConfig<RandomConfig> commonRandomConfig;

    public static SCJsonConfig<RandomConfig> serverRandomConfig;

    public static SCJsonConfig<RandomConfig> clientRandomConfig;

    public static void init() {

        commonRandomConfig = (SCJsonConfig<RandomConfig>)
                SCJsonConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.COMMON)
                        .build();

        serverRandomConfig = (SCJsonConfig<RandomConfig>)
                SCJsonConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.SERVER)
                        .build();

        clientRandomConfig = (SCJsonConfig<RandomConfig>)
                SCJsonConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.CLIENT)
                        .build();

        var var = clientRandomConfig.getCurrentConfig().randomValue;
        ShadowConfig.LOGGER.info(var);
        System.out.println(var);
    }

}