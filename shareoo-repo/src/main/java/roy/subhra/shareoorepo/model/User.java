package roy.subhra.shareoorepo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.Set;


@Getter
@Setter

public class User {

    @Id
    private String regNumber;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;

}
