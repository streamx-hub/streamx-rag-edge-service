package com.streamx.blueprints.web.server;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import java.util.Optional;
import java.util.Set;

@ConfigMapping(prefix = "streamx.blueprints.openai-rag-sink")
public interface Configuration {

  Optional<String> defaultNamespace();

  Optional<Set<String>> htmlResourceTypes();

  @WithDefault("/tmp/streamx")
  String storageRootDirectory();
}
