package roy.subhra.shareoo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;


@Getter
public class RebalanceEvent extends AuditEvent {
    private ShareGroup shareGroup;

    public RebalanceEvent(@JsonProperty() String performedBy, ShareGroup shareGroup) {
        super(EventType.REBALANCE, performedBy);
        this.shareGroup = shareGroup;
    }

    public RebalanceEvent(@JsonProperty("eventDate") Date evntdt, @JsonProperty("eventPerformedBy") String performedBy,
                          @JsonProperty("shareGroup") ShareGroup shareGroup) {
        super(evntdt, EventType.REBALANCE, performedBy);
        this.shareGroup = shareGroup;
    }

}
