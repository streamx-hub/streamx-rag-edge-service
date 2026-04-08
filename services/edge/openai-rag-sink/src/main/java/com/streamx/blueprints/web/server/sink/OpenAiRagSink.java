package com.streamx.blueprints.web.server.sink;

import static com.streamx.blueprints.cloudevents.utils.CloudEventUtils.isPublishingType;
import static com.streamx.blueprints.cloudevents.utils.CloudEventUtils.isUnpublishingType;

import com.streamx.blueprints.cloudevents.utils.CloudEventUtils;
import com.streamx.blueprints.web.server.Channels;
import com.streamx.blueprints.web.server.Configuration;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import io.cloudevents.CloudEvent;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OpenAiRagSink {

  private static final String INDEX_HTML_SUFFIX = "index.html";
  public static final String META_SOURCE_URL = "source_url";

  private Set<String> htmlResourceTypes;
  private String defaultNamespace;

  @Inject
  Logger log;

  @Inject
  Configuration config;

  @Inject
  EmbeddingStore<TextSegment> embeddingStore;

  @Inject
  EmbeddingModel embeddingModel;

  @PostConstruct
  void init() {
    this.htmlResourceTypes = config.htmlResourceTypes().orElseGet(Collections::emptySet);
    this.defaultNamespace = config.defaultNamespace().orElse("");
  }

  @Incoming(Channels.RESOURCES)
  public Uni<Void> consume(CloudEvent event) {
    String subject = CloudEventUtils.getSubject(event);
    Document document;
    try {
      document = CloudEventUtils.getDataSkippingUnknownProperties(event, Document.class);
    } catch (IllegalStateException e) {
      log.warnf(e, "Unsupported event: subject %s, type %s", subject, event.getType());
      return Uni.createFrom().voidItem();
    }
    return process(document, subject, event.getType(),
        Objects.requireNonNull(event.getTime()).toInstant().toEpochMilli());
  }

  private Uni<Void> process(Document document, String subject, String type, long eventTime) {
    boolean isHtmlResource = htmlResourceTypes.contains(type);
    String path = getPathFrom(subject, isHtmlResource);
    log.tracef("Storing %s resource: subject %s, type %s, event time %s under path %s",
        (isHtmlResource ? "HTML" : "non-HTML"), subject, type, eventTime, path);
    return updateStorage(document, path, type);
  }

  private Uni<Void> updateStorage(Document document, String path, String type) {
    if (isPublishingType(type)) {
      EmbeddingStoreIngestor ingestor = buildIngestor();
      ingestor.ingest(document);
      log.tracef("File updated: %s", path);
    }
    if (isUnpublishingType(type)) {
      removeByUrl(path);
      log.tracef("File deleted: %s", path);
    }
    return Uni.createFrom().voidItem();
  }

  private void removeByUrl(String url) {
    try {
      embeddingStore.removeAll(
          MetadataFilterBuilder.metadataKey(META_SOURCE_URL)
              .isEqualTo(url));
    } catch (Exception e) {
      log.debugf("removeAll by source_url not supported or no vectors found for %s: %s",
          url, e.getMessage());
    }
  }

  private String getPathFrom(String subject, boolean isHtmlResource) {
    String namespace = CloudEventUtils.getSubjectNamespace(subject).orElse(defaultNamespace);
    String path = namespace + "/" + CloudEventUtils.getSubjectWithoutNamespace(subject);
    return isHtmlResource ? computeHtmlResourcePath(path) : path;
  }

  static String computeHtmlResourcePath(String path) {
    if (path.endsWith("/")) {
      return path + INDEX_HTML_SUFFIX;
    }
    if (FilenameUtils.getExtension(path).isEmpty()) {
      return path + "/" + INDEX_HTML_SUFFIX;
    }
    return path;
  }

  private EmbeddingStoreIngestor buildIngestor() {
    return EmbeddingStoreIngestor.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .documentSplitter(DocumentSplitters.recursive(
            config.ingestion().chunkSize(),
            config.ingestion().chunkOverlap()
        ))
        .build();
  }
}
