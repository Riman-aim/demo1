package com.example.demo.controller;


import com.example.demo.domain.Address;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/UserManaging")
public class UserController {

    @Autowired
    private UserService userService;



    @PostMapping("/saveUser")
    public User saveUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @GetMapping("/getPassword/{username}")
    public String getPassword(@PathVariable String username){
        return userService.getPasswordByUsername(username);
    }

    @GetMapping("/getAllInfo/{userId}")
    public User getAllInfo(@PathVariable Long userId) {
        return userService.findById(userId);
    }


//    //از این برای اپدیت کردن و ویرایش اطلاعات کاربران استفاده میشه در واقعیت ؟
    @PatchMapping("/updateFields/{id}")
    public void updateUser(@PathVariable Long id,@RequestBody User user) {

        userService.updateUser(id ,user);
    }

    @PatchMapping("/addAddress/{id}")
    public void addAddress(@PathVariable Long id,@RequestBody Address address) {
        userService.addAddressById(id,address);
    }

    @PatchMapping("/updateAddress/{userId}")
    public void updateAddress(@PathVariable Long userId,@RequestBody Address address) {
        userService.updateAddress(userId,address);
    }

    @PatchMapping("/acceptUser/{adminId}/{userId}")
    public void acceptUser(@PathVariable Long adminId,@PathVariable Long userId) {
        userService.acceptUserById(adminId,userId);
    }

    @GetMapping("/signIn/{username}/{password}")
    public String signIn(@PathVariable String username, @PathVariable String password) {
        return userService.signIn(username,password);
    }




}
