package roy.subhra.shareoo.repo.controllers;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import roy.subhra.shareoo.model.*;
import roy.subhra.shareoo.repo.exceptions.ResourceNotFoundException;
import roy.subhra.shareoo.repo.repo.ShareGroupRepository;
import roy.subhra.shareoo.repo.repo.UserRepository;

import java.util.*;


@RequestMapping("/users")
@RestController
public class ShareooRepoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShareGroupRepository shareGroupRepository;
    @Autowired
    EventFactory eventFactory;

    @GetMapping(path = "/{id}")
    public @ResponseBody Optional<User> findById(@PathVariable String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return optionalUser;
        }else{
            throw new ResourceNotFoundException("User Not Found");
        }

    }

    @PostMapping(path = "")
    public @ResponseBody User registerNewUser (@RequestBody  User user){
        Optional<User> optionalUser = userRepository.findById(user.getPhoneNumber());
        Optional<User> optionalUser2 = userRepository.findById(user.getEmailId());
        if(optionalUser.isPresent() || optionalUser2.isPresent()){
            throw new DuplicateKeyException("User already Exists!");
        }
        if(StringUtils.isEmpty(user.getRegNumber())){
            user.setRegNumber(null);
        }
        return userRepository.save(user);
    }


    @GetMapping(path = "/{id}/sharegroups")
    public @ResponseBody Iterable<ShareGroup> findCurrentShareGroups(@PathVariable String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
           return shareGroupRepository.findAllByMembersContains(optionalUser.get().getRegNumber());
            //return shareGroupRepository.findAll();
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @GetMapping(path = "/{id}/sharegroups/{id2}")
    public @ResponseBody Optional<ShareGroup> findShareGroupById(@PathVariable String id, @PathVariable String id2 ){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return shareGroupRepository.findByShareGroupIdAndMembersContains(id2,optionalUser.get().getRegNumber());
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @PutMapping(path = "/{id}/sharegroups/{id2}")
    public @ResponseBody Optional<ShareGroup> updateShareGroup(@PathVariable String id, @PathVariable String id2 , @RequestBody ShareGroup group){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Optional<ShareGroup>  grp = shareGroupRepository.findByShareGroupIdAndMembersContains(id2,optionalUser.get().getRegNumber());
            if(grp.isPresent()){
                shareGroupRepository.save(group);
            }else{
                throw new ResourceNotFoundException("User Group Not Found!");
            }
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
        return Optional.of(group);
    }

    @PostMapping(path = "/{id}/sharegroups")
    public @ResponseBody ShareGroup createShareGroup(@PathVariable String id, @RequestBody ShareGroup shareGroup){

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            if(shareGroup.getMembers() == null){
                shareGroup.setMembers(new HashSet<>());
            }
            shareGroup.getMembers().add(optionalUser.get().getRegNumber());//Make Creator a member
            shareGroup.setCreatedBy(optionalUser.get().getRegNumber());
            shareGroup.setExpenses(new ArrayList<>());
            shareGroup.setLiabilities(new HashMap<>());
            shareGroup.setSettlements(new ArrayList<>());
            shareGroup.setActive(true);
            return shareGroupRepository.save(shareGroup);
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @GetMapping(path = "/{id}/sharegroups/{id2}/members")
    public @ResponseBody Iterable<User> findShareGroupMembers(@PathVariable String id, @PathVariable String id2 ){
            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                return userRepository.findAllById(optionalShareGroup.get().getMembers());
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }

    }

    @GetMapping(path = "/{id}/sharegroups/{id2}/expenses")
    public @ResponseBody List<Expense> findShareGroupExpenses(@PathVariable String id, @PathVariable String id2 ){


            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                return optionalShareGroup.get().getExpenses();
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }

    }

    @PostMapping(path = "/{id}/sharegroups/{id2}/members/{idToAdd}")
    public @ResponseBody Iterable<User> addGroupMember(@PathVariable String id, @PathVariable String id2,@PathVariable String idToAdd ){

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                Optional<User> optionalAddUser  =  userRepository.findById(idToAdd);
                if(optionalAddUser.isPresent()){
                    ShareGroup shareGroup = optionalShareGroup.get();
                    if (shareGroup.getMembers() == null){
                        shareGroup.setMembers(new HashSet<>());
                    }
                    shareGroup.getMembers().add(optionalAddUser.get().getRegNumber());
                    shareGroup.getLiabilities().put(optionalAddUser.get().getRegNumber(),new HashMap<>());

                    shareGroup = shareGroupRepository.save(shareGroup);
                    eventFactory.raiseEvent(EventType.REBALANCE,optionalUser.get().getRegNumber(),shareGroup);
                    return findShareGroupMembers(id,id2);
                }else {
                    throw new ResourceNotFoundException("User to Add Not Found!");
                }
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @PostMapping(path = "/{id}/sharegroups/{id2}/expenses")
    public @ResponseBody List<Expense> addExpenseToShareGroup(@PathVariable String id, @PathVariable String id2,@RequestBody Expense expense){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                ShareGroup shareGroup = optionalShareGroup.get();
                expense.setCreatedBy(optionalUser.get().getRegNumber());
                shareGroup.getExpenses().add(expense);
                shareGroup = shareGroupRepository.save(shareGroup);
                eventFactory.raiseEvent(EventType.REBALANCE,optionalUser.get().getRegNumber(),shareGroup);
                return shareGroup.getExpenses();
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @GetMapping(path = "/{id}/sharegroups/{id2}/settlements")
    public @ResponseBody Iterable<Settlement> findSettlementsForUser(@PathVariable String id, @PathVariable String id2){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                return optionalShareGroup.get().getSettlements();
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }
    @PostMapping(path = "/{id}/sharegroups/{id2}/settlements")
    public @ResponseBody List<Settlement> settleBill (@PathVariable String id,@PathVariable String id2, @RequestBody Settlement settlement){
        Optional<User> optionalUserTo = userRepository.findById(settlement.getPaidTo());
        Optional<User> optionalUserFrom = userRepository.findById(id);
        Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
        if(optionalUserTo.isPresent() && optionalUserFrom.isPresent() && optionalShareGroup.isPresent() ){
            settlement.setPaidBy(optionalUserFrom.get().getRegNumber());
            settlement.setPaidTo(optionalUserTo.get().getRegNumber());
            ShareGroup shareGroup = optionalShareGroup.get();
            if(shareGroup.getSettlements() == null){
                shareGroup.setSettlements(new ArrayList<>());
            }
            shareGroup.getSettlements().add(settlement);
            shareGroup = shareGroupRepository.save(shareGroup);
            eventFactory.raiseEvent(EventType.REBALANCE,optionalUserFrom.get().getRegNumber(),shareGroup);
            return  shareGroup.getSettlements();
        }else {
            throw new ResourceNotFoundException("User or ShareGroup Not Found!");
        }
    }
}
