package com.example.demo.service;

import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.domain.User;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;


    public void acceptUserById(Long adminId, Long userId) {
        if (adminRepository.existsById(adminId) && userService.existById(userId)) {
            userService.acceptUserById(userId);
        } else {
            throw new UserNotFoundException
                    ("user not found by ID: " + userId + " or admin not found by ID: " + adminId);
        }
    }

    public List<User> getNotAcceptedUsers() {
        return userService.getNotAcceptedUsers();
    }

}
