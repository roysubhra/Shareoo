package roy.subhra.shareoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import roy.subhra.shareoo.model.RebalanceEvent;
import roy.subhra.shareoo.model.Settlement;
import roy.subhra.shareoo.model.ShareGroup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RebalanceService {
    @Autowired
    private RestURIHelper helper;

    @Autowired
    private RestTemplate restTemplate;

    @StreamListener(value = ShareooChannels.REBALANCE)
    public void rebalance(@Payload RebalanceEvent event) {


        final ShareGroup group = event.getShareGroup();
        final Map<String, BigDecimal> expenses = new HashMap<>();
        BigDecimal count = new BigDecimal(group.getMembers().size());
        final List<Settlement> settlements = group.getSettlements();

        sumExpensesByMember(group, expenses);

        final BigDecimal share = expenses.values().stream().reduce((a, b) -> a.add(b)).get().divide(count).setScale(2, BigDecimal.ROUND_UP);

        final Map<String, BigDecimal> balances = calculateIndividualBalances(group, expenses, settlements);

        group.getMembers().forEach(memberId -> calculateLiability(group, share, balances, memberId));

        restTemplate.exchange(helper.uriSupplier.get() + "users/" + event.getEventPerformedBy() + "/sharegroups/" + group.getShareGroupId(), HttpMethod.PUT, new HttpEntity<ShareGroup>(group), ShareGroup.class);
    }

    private void calculateLiability(ShareGroup group, BigDecimal share, Map<String, BigDecimal> balances, String memberId) {
        BigDecimal myBalance = balances.get(memberId);
        final Map<String, BigDecimal> liabilities = group.getLiabilities().get(memberId);
        liabilities.clear();
        if (myBalance.compareTo(share) == -1) {
            BigDecimal myDue = share.subtract(myBalance).setScale(2, BigDecimal.ROUND_UP);
            Map<String, BigDecimal> payTo = balances.entrySet().stream().filter(e -> !e.getKey().equals(memberId) && e.getValue().compareTo(share) == 1).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            for (Map.Entry<String, BigDecimal> payToEntry : payTo.entrySet()) {
                String targetMember = payToEntry.getKey();
                if (payToEntry.getValue().compareTo(myDue) < 0) {
                    myDue = myDue.subtract(payToEntry.getValue());
                    liabilities.put(targetMember, payToEntry.getValue());
                    balances.put(targetMember, BigDecimal.ZERO);
                } else {
                    liabilities.put(targetMember, myDue);
                    balances.put(targetMember, balances.get(targetMember).subtract(myDue));
                    balances.put(memberId, BigDecimal.ZERO);
                    break;
                }
            }

        }
    }

    private Map<String, BigDecimal> calculateIndividualBalances(ShareGroup group, Map<String, BigDecimal> expenses, List<Settlement> settlements) {
        return group.getMembers().stream().collect(Collectors.toMap(m -> m, m -> {
            String memberId = (String) m;
            group.getLiabilities().put(memberId, new HashMap<>());

            BigDecimal myBalance = expenses.get(memberId);
            if (myBalance == null) {
                myBalance = BigDecimal.ZERO;
            }
            return settlements.size() > 0 ? myBalance.add(settlements.stream().filter(s -> s.getPaidBy().equals(memberId) || s.getPaidTo().equals(memberId)).map(s -> s.getPaidTo().equals(memberId) ? s.getAmount().negate() : s.getAmount()).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO)) : myBalance;
        }));
    }

    private void sumExpensesByMember(ShareGroup group, Map<String, BigDecimal> expenses) {
        group.getExpenses().forEach(exp -> {
            if (expenses.containsKey(exp.getCreatedBy())) {
                expenses.get(exp.getCreatedBy()).add(exp.getAmount());
            } else {
                expenses.put(exp.getCreatedBy(), exp.getAmount());
            }
        });
    }
}
