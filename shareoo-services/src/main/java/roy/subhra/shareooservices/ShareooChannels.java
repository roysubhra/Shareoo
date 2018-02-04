package roy.subhra.shareooservices;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ShareooChannels {
    String REBALANCE = "REBALANCE";


    @Input("REBALANCE")
    SubscribableChannel rebalance();



}
