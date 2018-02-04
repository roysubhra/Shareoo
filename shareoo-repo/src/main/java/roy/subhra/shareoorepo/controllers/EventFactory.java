package roy.subhra.shareoorepo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.*;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import roy.subhra.shareoorepo.ShareooChannels;
import roy.subhra.shareoorepo.model.*;

@Component
@EnableBinding(ShareooChannels.class)
public class EventFactory {

@Autowired
EventGateway gateway;



    public  void raiseEvent(EventType eventType, String performedBy, Object... args) {
        switch (eventType){
            case REBALANCE:
                gateway.raiseRebalanceEvent(buildRebalacnceEvent(performedBy,(ShareGroup)args[0]));
                break;


        }
    }
    private  RebalanceEvent buildRebalacnceEvent(String performedBy, ShareGroup shareGroup){
        return  new RebalanceEvent(performedBy,shareGroup);
    }
}
