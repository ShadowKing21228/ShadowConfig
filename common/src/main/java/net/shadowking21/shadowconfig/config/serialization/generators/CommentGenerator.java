package net.shadowking21.shadowconfig.config.serialization.generators;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import net.shadowking21.shadowconfig.annotation.ConfigComment;

import java.io.IOException;
import java.lang.reflect.Field;

public class CommentGenerator extends JsonGeneratorDelegate {
    private final Object bean;
    private final String commentPrefix;
    private final java.util.Deque<Boolean> firstFieldStack = new java.util.ArrayDeque<>();
    private String pendingComment;

    public CommentGenerator(JsonGenerator d, Object bean, String commentPrefix) {
        super(d);
        this.bean = bean;
        this.commentPrefix = commentPrefix;

        if ("//".equals(commentPrefix)) {
            PrettyPrinter base = d.getPrettyPrinter();
            DefaultPrettyPrinter printer = base instanceof DefaultPrettyPrinter
                    ? (DefaultPrettyPrinter) base
                    : new DefaultPrettyPrinter();
            setPrettyPrinter(new CommentingPrettyPrinter(printer, this));
        }
    }

    @Override
    public void writeStartObject() throws IOException {
        firstFieldStack.push(true);
        super.writeStartObject();
    }

    @Override
    public void writeStartObject(Object forValue) throws IOException {
        firstFieldStack.push(true);
        super.writeStartObject(forValue);
    }

    @Override
    public void writeEndObject() throws IOException {
        if (!firstFieldStack.isEmpty()) {
            firstFieldStack.pop();
        }
        super.writeEndObject();
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        Field f = findField(resolveCurrentBean(), name);
        if (f != null) {
            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            if (comment != null) {
                if ("//".equals(commentPrefix) && getPrettyPrinter() instanceof CommentingPrettyPrinter printer) {
                    if (isFirstFieldInObject()) {
                        printer.writeLeadingComment(delegate, comment.value());
                    }
                    else {
                        pendingComment = comment.value();
                    }
                }
                else {
                    writeRawCommentLines(comment.value());
                }
            }
        }

        super.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        if (delegate instanceof YAMLGenerator)
        {
            super.writeFieldName(name);
            return;
        }

        Field f = findField(resolveCurrentBean(), name.getValue());
        if (f != null) {
            ConfigComment comment = f.getAnnotation(ConfigComment.class);

            if (comment != null) {
                if ("//".equals(commentPrefix) && getPrettyPrinter() instanceof CommentingPrettyPrinter printer) {
                    if (isFirstFieldInObject()) {
                        printer.writeLeadingComment(delegate, comment.value());
                    }
                    else {
                        pendingComment = comment.value();
                    }
                }
                else {
                    writeRawCommentLines(comment.value());
                }
            }
        }
        super.writeFieldName(name);
    }

    private Object resolveCurrentBean() {
        Object current = currentValue();
        return current != null ? current : bean;
    }

    private Field findField(Object target, String fieldName) {
        if (target == null || fieldName == null) {
            return null;
        }

        Class<?> type = target.getClass();
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

    private boolean isFirstFieldInObject() {
        if (firstFieldStack.isEmpty()) {
            return false;
        }

        if (Boolean.TRUE.equals(firstFieldStack.peek())) {
            firstFieldStack.pop();
            firstFieldStack.push(false);
            return true;
        }

        return false;
    }

    private String consumePendingComment() {
        String value = pendingComment;
        pendingComment = null;
        return value;
    }

    private String getCommentPrefix() {
        return commentPrefix;
    }

    private void writeRawCommentLines(String comment) throws IOException {
        String[] lines = comment.replace("\r\n", "\n").split("\n", -1);
        for (String line : lines) {
            delegate.writeRaw(commentPrefix + " " + line + "\n");
        }
    }

    private static final class CommentingPrettyPrinter extends DefaultPrettyPrinter {
        private final CommentGenerator owner;

        private CommentingPrettyPrinter(DefaultPrettyPrinter base, CommentGenerator owner) {
            super(base);
            this.owner = owner;
        }

        @Override
        public void writeObjectEntrySeparator(JsonGenerator g) throws IOException {
            super.writeObjectEntrySeparator(g);
            String comment = owner.consumePendingComment();
            if (comment != null) {
                writeCommentLines(g, comment);
                _objectIndenter.writeIndentation(g, _nesting);
            }
        }

        private void writeLeadingComment(JsonGenerator g, String comment) throws IOException {
            _objectIndenter.writeIndentation(g, _nesting);
            writeCommentLines(g, comment);
        }

        private void writeCommentLines(JsonGenerator g, String comment) throws IOException {
            String[] lines = comment.replace("\r\n", "\n").split("\n", -1);
            for (int i = 0; i < lines.length; i++) {
                if (i > 0) {
                    //g.writeRaw("\n");
                    _objectIndenter.writeIndentation(g, _nesting);
                }
                g.writeRaw(owner.getCommentPrefix() + " " + lines[i]);
            }
        }
    }
}
