package roy.subhra.shareoo.repo;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ShareooChannels {

    String REBALANCE = "REBALANCE";



    @Output("REBALANCE")
    MessageChannel rebalance();


}
