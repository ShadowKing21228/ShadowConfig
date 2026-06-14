package net.shadowking21.shadowconfig.config.builder.stages;

public interface ClazzStage<T> {
    DefaultsStage<T> clazz(Class<T> clazz);
}
