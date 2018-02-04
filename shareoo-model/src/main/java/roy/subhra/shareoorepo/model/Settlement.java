package roy.subhra.shareoorepo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Settlement {
    @Id
    private String settlementId;
    private String paidBy;
    private String paidTo;
    private Date paymentDate;
    private BigDecimal amount;
}
