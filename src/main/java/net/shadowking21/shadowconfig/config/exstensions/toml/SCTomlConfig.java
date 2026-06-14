package net.shadowking21.shadowconfig.config.exstensions.toml;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;
import net.shadowking21.shadowconfig.config.builder.BaseConfigBuilder;
import net.shadowking21.shadowconfig.config.builder.stages.ClazzStage;
import net.shadowking21.shadowconfig.config.builder.stages.DefaultsStage;
import net.shadowking21.shadowconfig.config.builder.stages.ModIdStage;
import net.shadowking21.shadowconfig.config.builder.stages.OptionalStage;

import java.nio.file.Path;

public class SCTomlConfig<T> extends BaseShadowConfig<T> {
    public SCTomlConfig(String modId, Path path, T defaults, Class<T> clazz, ConfigSide configSide, ObjectMapper mapper) {
        super(modId, path, defaults, clazz, configSide, mapper);
    }

    public SCTomlConfig(String modId, T defaults, Class<T> clazz, ConfigSide configSide, ObjectMapper mapper) {
        super(modId, defaults, clazz, configSide, mapper);
    }

    public SCTomlConfig(String modId, T defaults, Class<T> clazz, ConfigSide configSide) {
        super(modId, defaults, clazz, configSide, ShadowConfig.getDefaultTomlMapper());
    }

    @Override
    protected String getExtension() {
        return ".toml";
    }

    @Override
    public boolean isCommentsAllowed() {
        return true;
    }

    @Override
    public String getCommentPrefix() {
        return "#";
    }

    public static class Builder<T> extends BaseConfigBuilder<T> implements ModIdStage<T>, ClazzStage<T>, DefaultsStage<T>, OptionalStage<T>
    {
        private Builder() {}

        public static <T> OptionalStage<T> builder(Class<T> clazz, T defaults, String modId) {
            var builder = new Builder<T>();
            builder.setDefaults(defaults);
            builder.setClass(clazz);
            builder.setModId(modId);
            return builder;
        }

        public static <T> ModIdStage<T> builder(Class<T> clazz, T defaults) {
            var builder = new Builder<T>();
            builder.setDefaults(defaults);
            builder.setClass(clazz);
            return builder;
        }

        public static <T> DefaultsStage<T> builder(Class<T> clazz)
        {
            var builder = new Builder<T>();
            builder.setClass(clazz);
            return builder;
        }

        public static <T> ClazzStage<T> builder() {
            return new Builder<>();
        }

        @Override
        public OptionalStage<T> modId(String modId) {
            setModId(modId);
            return this;
        }

        @Override
        public DefaultsStage<T> clazz(Class<T> clazz) {
            setClass(clazz);
            return this;
        }

        @Override
        public OptionalStage<T> path(Path path) {
            setPath(path);
            return this;
        }

        @Override
        public OptionalStage<T> mapper(ObjectMapper mapper) {
            setMapper(mapper);
            return this;
        }

        @Override
        public OptionalStage<T> side(ConfigSide side) {
            setSide(side);
            return this;
        }

        @Override
        public BaseShadowConfig<T> build() {
            var config = new SCTomlConfig<>(modId, path, defaults, clazz, side, mapper);
            config.init();
            return config;
        }

        @Override
        public ModIdStage<T> defaults(T defaults) {
            setDefaults(defaults);
            return this;
        }

        @Override
        protected ObjectMapper getDefaultMapper() {
            return ShadowConfig.getDefaultTomlMapper();
        }
    }
}
