package roy.subhra.shareoorepo.repo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roy.subhra.shareoorepo.model.ShareGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface ShareGroupRepository  extends CrudRepository<ShareGroup, String> {


    Iterable<ShareGroup> findAllByMembersContains(@Param("members") String memberId);

    @Query("{{'members : {$all:?0}'},{'active':true}}")
    Set<ShareGroup> findAllByMembersContainsAll(@Param("members") List<String> memberIds);



    Optional<ShareGroup> findByShareGroupIdAndMembersContains(@Param("members") String memberId, @Param("_id")String shareGroupId);
}
