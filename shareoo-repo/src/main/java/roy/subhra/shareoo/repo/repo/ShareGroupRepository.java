package roy.subhra.shareoo.repo.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roy.subhra.shareoo.model.ShareGroup;

import java.util.Optional;


public interface ShareGroupRepository  extends CrudRepository<ShareGroup, String> {


    Iterable<ShareGroup> findAllByMembersContains(@Param("members") String memberId);


    Optional<ShareGroup> findByShareGroupIdAndMembersContains(@Param("members") String memberId, @Param("_id")String shareGroupId);
}
