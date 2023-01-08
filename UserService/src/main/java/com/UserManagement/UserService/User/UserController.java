package com.UserManagement.UserService.User;

import com.UserManagement.UserService.S3BucketStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@EnableScheduling
@RestController
@RequestMapping(path = "api/v1/user-management")
public class UserController {
    @Autowired
    S3BucketStorageService s3BucketStorageService;

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

    @GetMapping("/list/files")
    public ResponseEntity<List<String>> getListOfFiles() {
        return new ResponseEntity<>(s3BucketStorageService.listFiles(), HttpStatus.OK);
    }

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName") String fileName,
                                             @RequestParam("file") MultipartFile file) {
        LOG.info("Uploading file",fileName);
        return new ResponseEntity<>(s3BucketStorageService.uploadFile(fileName, file), HttpStatus.OK);
    }

    @DeleteMapping("/file/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileName") String fileName){
        LOG.info("Deleting file",fileName);
        return new ResponseEntity<>(s3BucketStorageService.deleteFile(fileName),HttpStatus.OK);
    }
}
