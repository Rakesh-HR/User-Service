package com.UserManagement.UserService.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user-management")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path="/view")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path="/search/{userId}")
    public User getUser(@PathVariable("userId") Long userId){
        return userService.getUser(userId);
    }
    @DeleteMapping(path = "/delete/{id}")
    public void deleteUserByID(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }

    @PostMapping(path="/add")
    public void addNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }
    @PutMapping(path = "/edit/{id}")
    public void changeUserPhNo(
            @PathVariable("id") Long id,
            @RequestParam(required = true) String userPhNo){
        userService.changeUserName(id,userPhNo);
    }
}
