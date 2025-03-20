package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminManaging")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PatchMapping("/acceptUser/{adminId}/{userId}")
    public void acceptUser(@PathVariable Long adminId, @PathVariable Long userId) {
        adminService.acceptUserById(adminId,userId);
    }

    @GetMapping("/getUnAcceptedUsers")
    public List<User> getUnAcceptedUsers() {
        return adminService.getNotAcceptedUsers();
    }


}
