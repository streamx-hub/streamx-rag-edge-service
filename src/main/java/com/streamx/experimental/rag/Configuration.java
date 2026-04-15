package com.streamx.experimental.rag;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import java.util.Optional;
import java.util.Set;

@ConfigMapping(prefix = "streamx.experimental.openai-rag-sink")
public interface Configuration {

  Optional<String> defaultNamespace();

  Optional<Set<String>> htmlResourceTypes();

  IngestionConfig ingestion();

  ChatProfile chatProfile();

  interface IngestionConfig {

    @WithDefault("500")
    int chunkSize();

    @WithDefault("50")
    int chunkOverlap();
  }

  interface ChatProfile {

    Optional<String> name();

    String displayName();

    boolean active();

    String systemPrompt();
  }
}
