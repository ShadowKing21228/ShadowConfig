package net.shadowking21.shadowconfig.config.exstensions.yaml.example;

import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.yaml.SCYamlConfig;
import net.shadowking21.shadowconfig.config.models.RandomConfig;

public class SCYamlTestConfig {
    public static SCYamlConfig<RandomConfig> commonRandomConfig;

    public static SCYamlConfig<RandomConfig> serverRandomConfig;

    public static SCYamlConfig<RandomConfig> clientRandomConfig;

    public static void init() {

        commonRandomConfig = (SCYamlConfig<RandomConfig>)
                SCYamlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.COMMON)
                        .build();

        serverRandomConfig = (SCYamlConfig<RandomConfig>)
                SCYamlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.SERVER)
                        .build();

        clientRandomConfig = (SCYamlConfig<RandomConfig>)
                SCYamlConfig.Builder.builder(RandomConfig.class)
                        .defaults(new RandomConfig())
                        .modId(ShadowConfig.MOD_ID)
                        .side(ConfigSide.CLIENT)
                        .build();

        var var = clientRandomConfig.getCurrentConfig().randomValue;
        ShadowConfig.LOGGER.info(var);
        System.out.println(var);
    }
}
