package roy.subhra.shareoorepo;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ShareooChannels {

    String REBALANCE = "REBALANCE";


    @Output("REBALANCE")
    MessageChannel rebalance();


}
