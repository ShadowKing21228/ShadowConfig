package net.shadowking21.shadowconfig.config.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;

import java.nio.file.Path;

public abstract class BaseConfigBuilder<T>
{
    protected String modId;
    protected Class<T> clazz;
    protected T defaults;
    protected Path path = ShadowConfig.getDefaultConfigPath();
    protected ObjectMapper mapper = getDefaultMapper();
    protected ConfigSide side = ConfigSide.COMMON;

    protected void setModId(String modId) {
        this.modId = modId;
    }

    protected void setPath(Path path) {
        this.path = path;
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected void setSide(ConfigSide side) {
        this.side = side;
    }

    protected void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected void setDefaults(T defaults) {
        this.defaults = defaults;
    }
    protected abstract ObjectMapper getDefaultMapper();
}

