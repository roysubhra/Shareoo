package roy.subhra.shareoorepo.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roy.subhra.shareoorepo.model.User;

import java.util.Optional;




public interface UserRepository extends CrudRepository<User, String> {
    @Query("{$or:[{'phoneNumber':?0},{'emailId':?0},{'regNumber':?0}]}, {$lookup:{from: 'ShareGroup',localField: '_id',foreignField: 'members',as: 'shareGroups'}}},{$lookup:{from: 'Settlements',localField: '_id',foreignField: 'paidBy',as: 'settlements'}} }")
    Optional<User> findById(String  id);

}
