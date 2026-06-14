package net.shadowking21.shadowconfig.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.annotation.ConfigComment;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Map;

public class YamlSerializer {

    private static final ObjectMapper mapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static void writeYaml(Object bean, Writer writer) throws IOException {
        Map<String, Object> map = mapper.convertValue(bean, Map.class);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        
        String rawYaml = yaml.dump(map);

        String finalYaml = insertComments(rawYaml, bean.getClass());

        writer.write(finalYaml);

        writer.flush();
        writer.close();
    }

    private static String insertComments(String yaml, Class<?> beanClass) {
        StringBuilder out = new StringBuilder();
        String[] lines = yaml.split("\n", -1);
        java.util.Deque<ClassContext> contextStack = new java.util.ArrayDeque<>();
        contextStack.push(new ClassContext(-1, beanClass));

        for (String line : lines) {
            if (line.isEmpty()) {
                out.append("\n");
                continue;
            }

            int indent = countLeadingSpaces(line);
            String trimmed = line.trim();

            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                out.append(line).append("\n");
                continue;
            }

            while (contextStack.size() > 1 && indent <= contextStack.peek().indent) {
                contextStack.pop();
            }

            String content = trimmed;
            if (content.startsWith("- ")) {
                content = content.substring(2).trim();
            }

            int colonIndex = content.indexOf(':');
            if (colonIndex > 0) {
                String key = content.substring(0, colonIndex).trim();
                Class<?> currentClass = contextStack.peek().type;
                String comment = extractComment(currentClass, key);
                if (comment != null) {
                    writeCommentLines(out, comment, indent);
                }

                String afterColon = content.substring(colonIndex + 1).trim();
                if (afterColon.isEmpty()) {
                    Class<?> nestedClass = resolveFieldType(currentClass, key);
                    if (nestedClass != null) {
                        contextStack.push(new ClassContext(indent, nestedClass));
                    }
                }
            }

            out.append(line).append("\n");
        }

        return out.toString();
    }

    private static String extractComment(Class<?> beanClass, String fieldName) {
        Field f = findField(beanClass, fieldName);
        if (f == null) {
            return null;
        }

        ConfigComment c = f.getAnnotation(ConfigComment.class);
        return c != null ? c.value() : null;
    }

    private static Field findField(Class<?> beanClass, String fieldName) {
        if (beanClass == null || fieldName == null) {
            return null;
        }

        Class<?> type = beanClass;
        while (type != null) {
            try {
                return type.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException ignored) {
                type = type.getSuperclass();
            }
        }

        return null;
    }

    private static Class<?> resolveFieldType(Class<?> beanClass, String fieldName) {
        Field field = findField(beanClass, fieldName);
        return field != null ? field.getType() : null;
    }

    private static int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }

    private static void writeCommentLines(StringBuilder builder, String comment, int indent) {
        String[] lines = comment.replace("\r\n", "\n").split("\n", -1);
        String indentSpaces = " ".repeat(Math.max(0, indent));
        for (String line : lines) {
            builder.append(indentSpaces).append("# ").append(line).append("\n");
        }
    }

    private static final class ClassContext {
        private final int indent;
        private final Class<?> type;

        private ClassContext(int indent, Class<?> type) {
            this.indent = indent;
            this.type = type;
        }
    }
}
