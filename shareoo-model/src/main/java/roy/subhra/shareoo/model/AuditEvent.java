package roy.subhra.shareoo.model;

import lombok.Getter;

import java.util.Date;

@Getter
public abstract class AuditEvent {
    private Date eventDate;
    private String eventPerformedBy;
    protected EventType eventType;

    AuditEvent(EventType evntType, String performedBy){
        eventDate = new Date();
        eventPerformedBy = performedBy;
        eventType = evntType;
    }

    AuditEvent(Date evntDate, EventType evntType, String performedBy){
        eventDate = evntDate;
        eventPerformedBy = performedBy;
        eventType = evntType;
    }
}
