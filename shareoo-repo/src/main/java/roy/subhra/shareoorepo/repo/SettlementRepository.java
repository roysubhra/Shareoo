package roy.subhra.shareoorepo.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roy.subhra.shareoorepo.model.Settlement;

import java.util.Set;

public interface SettlementRepository  extends CrudRepository<Settlement, String> {
    @Query("{{$or:[{'paidBy':?0},'paidTo':?0]},'$orderby':{'paymentDate':-1}}")
    Set<Settlement> findAllRelatedToMe(String userId);


}
