package net.shadowking21.shadowconfig.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.annotation.ConfigComment;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map;

public class YamlSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeYaml(Object bean, Writer writer) throws IOException {
        Map<String, Object> map = mapper.convertValue(bean, Map.class);

        Yaml yaml = new Yaml();
        String rawYaml = yaml.dump(map);

        String finalYaml = insertComments(rawYaml, bean.getClass());

        writer.write(finalYaml);

        writer.flush();
        writer.close();
    }

    private static String insertComments(String yaml, Class<?> beanClass) {
        StringBuilder out = new StringBuilder();
        String[] lines = yaml.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            // ищем строки вида "key:"
            String[] valuesKeyArray = trimmed.split(":", 2);
            String key = valuesKeyArray[0];
            String comment = extractComment(beanClass, key);

            if (comment != null) {
                out.append("# ").append(comment).append("\n");
            }


            out.append(line).append("\n");
        }

        return out.toString();
    }

    private static String extractComment(Class<?> beanClass, String fieldName) {
        try {
            Field f = beanClass.getDeclaredField(fieldName);
            ConfigComment c = f.getAnnotation(ConfigComment.class);
            return c != null ? c.value() : null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
