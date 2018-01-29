package roy.subhra.shareoorepo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roy.subhra.shareoorepo.model.Settlement;
import roy.subhra.shareoorepo.model.ShareGroup;
import roy.subhra.shareoorepo.repo.ShareGroupRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//TODO- Move to a separate MicroService and wire it reactively
@Service
public class SettlementService {

    @Autowired
    private ShareGroupRepository shareGroupRepo;

    public void settleBills(final Settlement settlement){
        List<String> userIds = new ArrayList<>();
        userIds.add(settlement.getPaidBy());
        userIds.add(settlement.getPaidTo());

        Set<ShareGroup> shareGroups = shareGroupRepo.findAllByMembersContainsAll(userIds);

        shareGroups = shareGroups.stream().filter(sg -> sg.isActive() && sg.getLiabilities().get(settlement.getPaidBy()).get(settlement.getPaidTo()).doubleValue()>0).collect(Collectors.toSet());
        BigDecimal balance = settlement.getAmount();

        Set<ShareGroup> updatedGroups = new HashSet<>();
        for(ShareGroup shareGroup:shareGroups){
            if(balance.equals(BigDecimal.ZERO)){
                break;
            }
            BigDecimal due = shareGroup.getLiabilities().get(settlement.getPaidBy()).get(settlement.getPaidTo());
            switch (due.compareTo(balance)){
                case 1:
                    due = due.subtract(balance);
                    break;
                case -1:
                    balance = balance.subtract(due);
                case 0:
                    due = BigDecimal.ZERO;
                    break;
            }
            shareGroup.getLiabilities().get(settlement.getPaidBy()).put(settlement.getPaidTo(),due);
            shareGroup.getSettlements().add(settlement.getSettlementId());
            updatedGroups.add(shareGroup);
        }
        shareGroupRepo.saveAll(updatedGroups);
    }
}
