package com.example.demo.controller;


import com.example.demo.domain.Address;
import com.example.demo.domain.SelfResponse;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/UserManaging")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @GetMapping("/getPassword/{username}")
    public ResponseEntity<Object> getPassword(@PathVariable String username){
        return new ResponseEntity<>(userService.getPasswordByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/getAllInfo/{userId}")
    public ResponseEntity<Object> getAllInfo(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
    }

    @PatchMapping("/updateFields/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,@RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(id ,user), HttpStatus.OK);
    }

//    @ResponseBody
    @PatchMapping("/addAddress/{id}")
    public ResponseEntity<Object> addAddress(@PathVariable Long id,@RequestBody Address address) {
        userService.addAddressById(id,address);
        return new ResponseEntity<>(
                userService.getSelfResponse(
                        "address successfully added","controller address method "), HttpStatus.CREATED);
//این کار درسته که تو کنترلر بیایم از دامینمون استفاده کنیم ؟
    }

    @PatchMapping("/updateAddress/{userId}")
    public void updateAddress(@PathVariable Long userId,@RequestBody Address address) {
        userService.updateAddress(userId,address);
    }

//در واقعیت وقتی که کاربری وارد اکانتش میشود نباید تمامی اطلاعاتش از دیتابی فچ شود ؟یعنی نباید خروجی متد زیر یک یوزر دی تی او باشد ؟
    @GetMapping("/signIn/{username}/{password}")
    public String signIn(@PathVariable String username, @PathVariable String password) {
        return userService.signIn(username,password);
    }

    @DeleteMapping("/deleteAccount/{id}")
    public void deleteAccount(@PathVariable Long id){
        userService.deleteUserById(id);
    }




}
