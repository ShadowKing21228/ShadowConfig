package net.shadowking21.shadowconfig.config.builder.stages;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shadowking21.shadowconfig.config.BaseShadowConfig;
import net.shadowking21.shadowconfig.config.ConfigSide;

import java.nio.file.Path;

public interface OptionalStage<T> {

    OptionalStage<T> path(Path path);

    OptionalStage<T> mapper(ObjectMapper mapper);

    OptionalStage<T> side(ConfigSide side);

    BaseShadowConfig<T> build();
}
