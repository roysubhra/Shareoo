package roy.subhra.shareoorepo.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ShareGroup {
    @Id
    private String shareGroupId;
    private String createdBy;
    private String name;
    private Set<String> members;
    private List<Expense> expenses;
    private Map<String,Map<String,BigDecimal>> liabilities;
    private List<Settlement> settlements;
    private boolean active;

}
