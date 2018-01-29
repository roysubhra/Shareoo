package roy.subhra.shareoorepo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
public class Expense {
    @Id
    private String receiptNo;
    private Date receiptDate;
    private String createdBy;
    private String description;
    private double amount;
}
