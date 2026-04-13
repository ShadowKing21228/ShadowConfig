package net.shadowking21.shadowconfig;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.json.example.SCJsonTestConfig;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ShadowConfig {

    public static final String MOD_ID = "shadowconfig";

    public static final Logger LOGGER = Logger.getLogger("ShadowConfig");

    private static SCPlatformHook CurrentPlatform;

    private static final List<BaseShadowConfig<?>> REGISTERED_CONFIGS = new ArrayList<>();

    public static void init() {
        if (CurrentPlatform.isDeveloper()) {
            SCJsonTestConfig.init();
            //SCJsoncTestConfig.init();
            //SCTomlTestConfig.init();
            //SCYamlTestConfig.init();
        }
    }

    public static Path getDefaultConfigPath() {
        if (CurrentPlatform == null) return null;
        return CurrentPlatform.getConfigPath();
    }

    public static ObjectMapper getDefaultJsonMapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
    }

    public static ObjectMapper getDefaultJsoncMapper() {
        var factory = JsonFactory.builder().enable(JsonReadFeature.ALLOW_JAVA_COMMENTS).build();
        return new ObjectMapper(factory)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
    }

    public static ObjectMapper getDefaultYamlMapper()
    {
        return new ObjectMapper(new YAMLFactory());
    }

    public static ObjectMapper getDefaultTomlMapper()
    {
        return new ObjectMapper(new TomlFactory());
    }

    public static ConfigSide getCurrentGameSide() {
        if (CurrentPlatform == null) return ConfigSide.COMMON;
        return CurrentPlatform.getCurrentSide();
    }

    public static void initPlatform(SCPlatformHook platformHook) {
        if (CurrentPlatform != null) return;
        CurrentPlatform = platformHook;

        for (var config : REGISTERED_CONFIGS) {
            config.init();
        }

        init();
    }

    public static void registerConfig(BaseShadowConfig<?> config) {
        REGISTERED_CONFIGS.add(config);
    }

    public static boolean isPlatformInitialized() {
        return CurrentPlatform != null;
    }

}
