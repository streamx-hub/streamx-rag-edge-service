package com.streamx.blueprints.web.server.sink;

import static com.streamx.blueprints.cloudevents.utils.CloudEventUtils.isPublishingType;
import static com.streamx.blueprints.cloudevents.utils.CloudEventUtils.isUnpublishingType;

import com.streamx.blueprints.cloudevents.utils.CloudEventUtils;
import com.streamx.blueprints.data.Resource;
import com.streamx.blueprints.web.server.Channels;
import com.streamx.blueprints.web.server.Configuration;
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

  @Inject
  Logger log;

  @Inject
  Configuration configuration;

  @PostConstruct
  void init() {

  }

  @Incoming(Channels.RESOURCES)
  public Uni<Void> consume(CloudEvent event) {
    return null; //TODO:
  }

}
