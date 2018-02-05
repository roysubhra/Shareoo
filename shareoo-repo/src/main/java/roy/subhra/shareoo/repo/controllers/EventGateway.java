package roy.subhra.shareoo.repo.controllers;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import roy.subhra.shareoo.model.RebalanceEvent;
import roy.subhra.shareoo.repo.ShareooChannels;

@Component
@MessagingGateway
public interface EventGateway {
    @Gateway(requestChannel = ShareooChannels.REBALANCE)
     void raiseRebalanceEvent(@Payload RebalanceEvent event);
}
