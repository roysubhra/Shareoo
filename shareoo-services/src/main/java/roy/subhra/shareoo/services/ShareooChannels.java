package roy.subhra.shareoo.services;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ShareooChannels {
    String REBALANCE = "REBALANCE";


    @Input("REBALANCE")
    SubscribableChannel rebalance();

}
