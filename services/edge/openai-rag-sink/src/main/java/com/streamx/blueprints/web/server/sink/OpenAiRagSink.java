package com.streamx.blueprints.web.server.sink;

import com.streamx.blueprints.web.server.Channels;
import com.streamx.blueprints.web.server.Configuration;
import io.cloudevents.CloudEvent;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OpenAiRagSink {

  @Inject
  Logger log;

  @Inject
  Configuration configuration;

  @PostConstruct
  void init() {

  }

  @Incoming(Channels.RESOURCES)
  public Uni<Void> consume(CloudEvent event) {
    return null;
  }

}
