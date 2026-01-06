package net.shadowking21.shadowconfig.config.serialization.generators;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import net.shadowking21.shadowconfig.annotation.ConfigComment;

import java.io.IOException;
import java.lang.reflect.Field;

public class CommentGenerator extends JsonGeneratorDelegate {
    private final Object bean;
    private final String commentPrefix;
    private boolean isFirstField = true;

    public CommentGenerator(JsonGenerator d, Object bean, String commentPrefix) {
        super(d);
        this.bean = bean;
        this.commentPrefix = commentPrefix;
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        try {
            Field f = bean.getClass().getDeclaredField(name);
            System.out.println("writeFieldName: " + name + " " + commentPrefix);

            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            if (comment != null) {
                delegate.writeRaw(commentPrefix + " " + comment.value() + "\n");
            }

        }
        catch (NoSuchFieldException ignored) {}
        super.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        if (delegate instanceof YAMLGenerator)
        {
            super.writeFieldName(name);
            return;
        }

        try {
            Field f = bean.getClass().getDeclaredField(name.getValue());
            //System.out.println("writeFieldName (serializableString): " + name + " " + commentPrefix);

            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            if (comment != null) {
                if (commentPrefix.equals("//") && isFirstField) {
                    delegate.writeRaw("\n" + commentPrefix + " " + comment.value());
                    isFirstField = false;
                }
                else if (commentPrefix.equals("//"))
                    delegate.writeRaw(",\n" + commentPrefix + " " + comment.value());
                else
                    delegate.writeRaw(commentPrefix + " " + comment.value() + "\n");
            }

        }
        catch (NoSuchFieldException ignored) {}
        super.writeFieldName(name);
    }
}
