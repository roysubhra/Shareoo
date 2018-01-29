package roy.subhra.shareoorepo.controllers;

import org.jboss.logging.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import roy.subhra.shareoorepo.exceptions.ResourceNotFoundException;
import roy.subhra.shareoorepo.model.Settlement;
import roy.subhra.shareoorepo.model.ShareGroup;
import roy.subhra.shareoorepo.model.User;
import roy.subhra.shareoorepo.repo.SettlementRepository;
import roy.subhra.shareoorepo.repo.ShareGroupRepository;
import roy.subhra.shareoorepo.repo.UserRepository;

import java.util.*;


@RequestMapping("/users")
@RestController
public class ShareooRepoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShareGroupRepository shareGroupRepository;
    @Autowired
    private SettlementRepository settlementRepository;


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
            shareGroup.getLiabilities().put(optionalUser.get().getRegNumber(),new HashMap<>());
            shareGroup.setSettlements(new ArrayList<>());
            return shareGroupRepository.save(shareGroup);
        }else{
            throw new ResourceNotFoundException("User Not Found!");
        }
    }

    @GetMapping(path = "/{id}/sharegroups/{id2}/members")
    public @ResponseBody Iterable<User> findShareGroupMembers(@PathVariable String id, @PathVariable String id2 ){

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Optional<ShareGroup> optionalShareGroup = findShareGroupById(id,id2);
            if(optionalShareGroup.isPresent()){
                return userRepository.findAllById(optionalShareGroup.get().getMembers());
            }else{
                throw new ResourceNotFoundException("ShareGroup Not Found!");
            }
        }else{
            throw new ResourceNotFoundException("User Not Found!");
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

                    shareGroupRepository.save(shareGroup);
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

    @GetMapping(path = "/{id}/settlements")
    public @ResponseBody Iterable<Settlement> findSettlementsForUser(@PathVariable String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return settlementRepository.findAllRelatedToMe(optionalUser.get().getRegNumber());
        }else {
            throw new ResourceNotFoundException("User Not Found!");
        }
    }
    @PostMapping(path = "/{fromId}/settlements/{toId}")
    public @ResponseBody Settlement settleBill (@PathVariable String fromId,@PathVariable String toId, @RequestBody Settlement settlement){
        Optional<User> optionalUserTo = userRepository.findById(fromId);
        Optional<User> optionalUserFrom = userRepository.findById(toId);
        if(optionalUserTo.isPresent() && optionalUserFrom.isPresent()){
            settlement.setPaidBy(optionalUserFrom.get().getRegNumber());
            settlement.setPaidTo(optionalUserTo.get().getRegNumber());
            //TODO-Trigger Settlement Event
            return settlementRepository.save(settlement);

        }else {
            throw new ResourceNotFoundException("User Not Found!");
        }
    }
}
