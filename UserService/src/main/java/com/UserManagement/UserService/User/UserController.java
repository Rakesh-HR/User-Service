package com.UserManagement.UserService.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@EnableScheduling
@RestController
@RequestMapping(path = "api/v1/user-management")
public class UserController {

   private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Cacheable(value = "users")
    @GetMapping(path="/view")
    public List<User> getUsers(){
        LOG.info("Getting all users");
        return userService.getUsers();
    }

    @Cacheable(value = "users")
    @GetMapping(path="/search/{userId}")
    public User getUser(@PathVariable("userId") long userId){
        LOG.info("Getting user with id "+userId);
        return userService.getUser(userId);
    }
    @CacheEvict(value = "users", allEntries=true)
    @DeleteMapping(path = "/delete/{id}")
    public void deleteUserByID(@PathVariable("id") long id){
        userService.deleteUser(id);
    }
    @CacheEvict(value = "users", allEntries=true)
    @DeleteMapping(path = "/deleteAll")
    public void deleteAll(){
        userService.deleteAll();
    }

    @PostMapping(path="/add")
    public void addNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }
    @CacheEvict(value = "users", key = "#id")
    @PutMapping(path = "/edit/{id}")
    public void changeUserPhNo(
            @PathVariable("id") long id,
            @RequestParam(required = true) String userPhNo){
        userService.changeUserName(id,userPhNo);
    }
    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "users", allEntries=true)
    public void evictAllcachesAtIntervals() {
            LOG.info("Cache cleared");
    }
}
