package roy.subhra.shareoo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


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
