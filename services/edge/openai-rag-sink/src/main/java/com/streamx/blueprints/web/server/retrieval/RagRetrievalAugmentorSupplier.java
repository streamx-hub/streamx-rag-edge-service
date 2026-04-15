package com.streamx.blueprints.web.server.retrieval;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.function.Supplier;

@ApplicationScoped
public class RagRetrievalAugmentorSupplier implements Supplier<RetrievalAugmentor> {

  @Inject
  EmbeddingStore<TextSegment> embeddingStore;

  @Inject
  EmbeddingModel embeddingModel;

  @Inject
  TranslatingQueryTransformer queryTransformer;

  @Override
  public RetrievalAugmentor get() {
    EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(10)
        .minScore(0.65)
        .build();

    return DefaultRetrievalAugmentor.builder()
        .queryTransformer(queryTransformer)
        .contentRetriever(contentRetriever)
        .build();
  }
}
