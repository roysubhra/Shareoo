package roy.subhra.shareoorepo.controllers;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import roy.subhra.shareoorepo.ShareooChannels;
import roy.subhra.shareoorepo.model.RebalanceEvent;

@Component
@MessagingGateway
public interface EventGateway {
    @Gateway(requestChannel = ShareooChannels.REBALANCE)
     void raiseRebalanceEvent(@Payload RebalanceEvent event);
}
