package net.shadowking21.shadowconfig;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.exstensions.json.example.SCJsonTestConfig;
import net.shadowking21.shadowconfig.config.exstensions.jsonc.example.SCJsoncTestConfig;
import net.shadowking21.shadowconfig.config.exstensions.toml.example.SCTomlTestConfig;
import net.shadowking21.shadowconfig.config.exstensions.yaml.example.SCYamlTestConfig;
import net.shadowking21.shadowconfig.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class ShadowConfig {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @SidedProxy(modId = Reference.MOD_ID, clientSide = "net.shadowking21.shadowconfig.proxy.ClientProxy", serverSide = "net.shadowking21.shadowconfig.proxy.CommonProxy")
    public static IProxy proxy;

    private static SCPlatformHook CurrentPlatform;

    private static final List<BaseShadowConfig<?>> REGISTERED_CONFIGS = new ArrayList<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initPlatform(new SCPlatformHook() {

            @Override
            public ConfigSide getCurrentSide() {
                return event.getSide() == Side.CLIENT ? ConfigSide.CLIENT : ConfigSide.SERVER;
            }

            @Override
            public Path getConfigPath() {
                return event.getModConfigurationDirectory().toPath();
            }

            @Override
            public boolean isDeveloper() {
                return FMLLaunchHandler.isDeobfuscatedEnvironment();
            }

        });
    }

    public static void init() {
        if (CurrentPlatform.isDeveloper()) {
            SCJsonTestConfig.init();
            SCJsoncTestConfig.init();
            SCTomlTestConfig.init();
            SCYamlTestConfig.init();
        }
    }

    public static Path getDefaultConfigPath() {
        if (CurrentPlatform == null) return null;
        return CurrentPlatform.getConfigPath();
    }

    public static ObjectMapper getDefaultJsonMapper() {
        return new ObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
    }

    public static ObjectMapper getDefaultJsoncMapper() {
        var factory = JsonFactory.builder().enable(JsonReadFeature.ALLOW_JAVA_COMMENTS).build();
        return new ObjectMapper(factory)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
    }

    public static ObjectMapper getDefaultYamlMapper()
    {
        return new ObjectMapper(new YAMLFactory())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static ObjectMapper getDefaultTomlMapper()
    {
        return new ObjectMapper(new TomlFactory())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
