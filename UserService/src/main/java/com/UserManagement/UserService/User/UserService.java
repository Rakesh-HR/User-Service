package com.UserManagement.UserService.User;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepositiory;

    @Autowired
    public UserService(UserRepository userRepositiory) {
        this.userRepositiory = userRepositiory;
    }

    public List<User> getUsers() {
        List<User> userList= userRepositiory.findAll();
        return userList;
    }

    public void deleteUser(Long id) {
        if(!userRepositiory.existsById(id))
            throw new IllegalStateException("User by id "+id+" does not exist");
        userRepositiory.deleteById(id);
    }

    public void addNewUser(User user) {
        List<User> list = userRepositiory.findAll();
        for(User tempUser : list){
            if(tempUser.getPhNo().equals(user.getPhNo()))
                throw new IllegalStateException("User with same Ph No already exists");
        }
        userRepositiory.save(user);
    }
    @Transactional
    public void changeUserName(Long id, String userPhNo) {
        User user =userRepositiory.findById(id).orElseThrow(
                ()-> new IllegalStateException(
                        "User with user id: "+id +" doe not exist"
                )
        );
        if(userPhNo!=null && userPhNo.length()>0)
            user.setPhNo(userPhNo);
    }

    public User getUser(Long userId) {
        List<User> userList= userRepositiory.findAll();
        for(User user : userList){
            if(user.getId().equals(userId))
                return user;
        }
        return null;
    }
}
