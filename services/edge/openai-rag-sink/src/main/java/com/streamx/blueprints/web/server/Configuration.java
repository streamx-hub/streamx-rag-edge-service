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

  IngestionConfig ingestion();

  interface IngestionConfig {

    @WithDefault("500")
    int chunkSize();

    @WithDefault("50")
    int chunkOverlap();
  }
}
