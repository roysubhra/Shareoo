package roy.subhra.shareoo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Expense {
    @Id
    private String receiptNo;
    private Date receiptDate;
    private String createdBy;
    private String description;
    private BigDecimal amount;

}
