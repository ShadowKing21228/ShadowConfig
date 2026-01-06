package net.shadowking21.shadowconfig.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.ShadowConfig;
import net.shadowking21.shadowconfig.config.serialization.generators.CommentGenerator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseShadowConfig<T> {

    protected final Path PATH;

    protected final Path FILE_PATH;

    protected ObjectMapper objectMapper;

    protected Class<T> configClass;

    protected T defaultConfig;

    protected T currentConfig;

    protected ConfigSide configSide;

    protected String modId;

    protected BaseShadowConfig(String modId, T defaults, Class<T> clazz, ConfigSide configSide, ObjectMapper mapper) {
        this(modId, ShadowConfig.getDefaultConfigPath(), defaults, clazz, configSide, mapper);
    }

    protected BaseShadowConfig(String modId, Path path, T defaults, Class<T> clazz, ConfigSide configSide, ObjectMapper mapper)
    {
        this.modId = modId;
        this.PATH = path;
        this.configSide = configSide;
        defaultConfig = defaults;
        objectMapper = mapper;
        configClass = clazz;
        FILE_PATH = Paths.get(path.toString(), getConfigName());
    }

    protected void init() {
        if (!isConfigAvailable())
            return;


        createPathsIfNotExists();
        if (!isExists())
            write(defaultConfig);
        else
            migrateIfNeed();

        currentConfig = read();
    }

    public T read()
    {
        configAllowThrow();

        T value;
        try {
            value = objectMapper.readValue(FILE_PATH.toFile(), configClass); // need change this cast
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    public void write(T value)
    {
        configAllowThrow();

        try {
            if (isCommentsAllowed()) {
                Writer writer = Files.newBufferedWriter(FILE_PATH);

                JsonGenerator gen = objectMapper.getFactory().createGenerator(writer);
                JsonGenerator commentingGen = new CommentGenerator(gen, value, getCommentPrefix());

                commentingGen.assignCurrentValue(value);

                objectMapper.writer().writeValue(commentingGen, value);

                commentingGen.flush();
                commentingGen.close();
            }
            else
                objectMapper.writeValue(FILE_PATH.toFile(), value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void rewrite(T value)
    {
        configAllowThrow();
        deleteConfigFile();

        try {
            if (isCommentsAllowed()) {
                Writer writer = Files.newBufferedWriter(FILE_PATH);

                var gen = objectMapper.getFactory().createGenerator(writer);
                var commentingGen = new CommentGenerator(gen, value, getCommentPrefix());

                objectMapper.writeValue(commentingGen, value);
            }
            else
                objectMapper.writeValue(FILE_PATH.toFile(), value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void rewriteToDefault()
    {
        rewrite(defaultConfig);
    }

    public void deleteConfigFile()
    {
        configAllowThrow();

        try {
            Files.deleteIfExists(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void migrateIfNeed()
    {
        configAllowThrow(); // Side check

        // If old config file does not have any fields of default config

        Field[] fields = configClass.getFields();

        // List of all fields for comparison from default class
        List<String> newConfigFields = Arrays.stream(fields)
                .map(Field::getName).toList();

        try {
            List<String> oldConfigFields = new ArrayList<>();

            // Missing fields from file, which exists in default config (class) but not in a current file
            // Used for gain default value in new config
            List<String> missingFields = new ArrayList<>();

            var tree = objectMapper.readTree(FILE_PATH.toFile());

            for (Field field : fields) {
                var name = field.getName();
                if (!tree.has(name))
                    continue;

                var node = tree.get(name);
                if (!node.isMissingNode()) {
                    oldConfigFields.add(name);
                }
                else if (node.isMissingNode()) {
                    missingFields.add(name);
                }
            }

            String newConfigString = newConfigFields.stream().sorted().collect(Collectors.joining(";"));
            String oldConfigString = oldConfigFields.stream().sorted().collect(Collectors.joining(";"));

            // If all existing sorted fields not equals, we needed a migration
            boolean isMigrationNeeded = !newConfigString.equals(oldConfigString);

            if (!isMigrationNeeded)
                return;

            T cfg = objectMapper.readValue(FILE_PATH.toFile(), configClass);

            for (Field f : configClass.getDeclaredFields()) {
                f.setAccessible(true);

                if (missingFields.contains(f.getName())) {
                    Object defaultValue = f.get(defaultConfig);
                    f.set(cfg, defaultValue);
                }
            }

            rewrite(cfg);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    public T getDefaultConfig()
    {
        configAllowThrow();
        return defaultConfig;
    }

    public T getCurrentConfig()
    {
        configAllowThrow();
        return currentConfig;
    }

    protected void createPathsIfNotExists()
    {
        try {
            if (!Files.exists(PATH)) {
                ShadowConfig.LOGGER.config("Directories with path " + PATH.toFile().getAbsolutePath() + " not found");
                Files.createDirectories(PATH);
            }
        }
        catch (Exception e) {
            ShadowConfig.LOGGER.config(e.getLocalizedMessage());
        }
    }

    protected boolean isExists()
    {
        return FILE_PATH.toFile().exists();
    }

    protected boolean isConfigAvailable()
    {
        return configSide == ShadowConfig.getCurrentGameSide() || configSide == ConfigSide.COMMON;
    }

    protected void configAllowThrow()
    {
        if (isConfigAvailable())
            return;

        throw new IllegalStateException("Attempt to access " + configSide + " config on the " + ShadowConfig.getCurrentGameSide() + " side");
    }

    public String getConfigName() {
        String sideName = switch (configSide) {
            case CLIENT -> "client";
            case SERVER -> "server";
            default -> "common";
        };
        return modId + "-" + sideName + getExtension();
    }

    public abstract boolean isCommentsAllowed();
    public abstract String getCommentPrefix();
    protected abstract String getExtension();
}
