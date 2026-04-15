package com.streamx.hub.rag.profile;

import com.streamx.hub.rag.chat.ChatResource;
import com.streamx.hub.rag.retrieval.RagRetrievalAugmentorSupplier;
import jakarta.enterprise.context.RequestScoped;

/**
 * Request-scoped holder for the chat profile resolved for the current HTTP request.
 *
 * <p>{@link ChatResource} resolves the profile from the
 * database once and stores it here. The
 * {@link RagRetrievalAugmentorSupplier} then reads
 * {@code maxResults} and {@code minScore} from this bean without an additional DB lookup.
 *
 * <p>CDI automatically creates one instance per HTTP request and tears it down
 * when the request completes. An {@code @ApplicationScoped} bean that injects this class receives a
 * transparent proxy that delegates to the correct per-request instance.
 */
@RequestScoped
public class ActiveProfile {

  private ChatProfile profile;

  public void set(ChatProfile profile) {
    this.profile = profile;
  }

  public ChatProfile get() {
    return profile;
  }

  /**
   * Convenience: retrieval max results, falls back to 10 if not yet set.
   */
  public int maxResults() {
    return profile != null ? profile.maxResults : ChatProfile.MAX_RESULTS;
  }

  /**
   * Convenience: retrieval min score, falls back to 0.50 if not yet set.
   */
  public double minScore() {
    return profile != null ? profile.minScore : 0.50;
  }
}
