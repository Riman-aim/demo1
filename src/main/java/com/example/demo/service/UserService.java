package com.example.demo.service;


import com.example.demo.Exception.UserNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.domain.Address;
import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.stereotype.Service;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressService addressService;

    public UserService() {
    }

    public UserService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteByID(Long id) {
        addressRepository.deleteById(id);
    }

    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found by ID: " + id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new RuntimeException("User not found by Username: " + username));
    }

    public Address getUserAddressById(Long id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.orElseThrow(() -> new RuntimeException("Address not found by UserID: " + id));
    }

    public Long getUserIDByUsername(String username) {
        Long userId = userRepository.getUserIdByUsername(username);
        if (userId != 0) {
            return userId;
        } else {
            throw new RuntimeException("UserID not found by Username: " + username);
        }
    }

    public String getPasswordByUsername(String username) {
        return userRepository.getPasswordByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found by username  " + username));
    }

    public boolean isTrueUSer(String username, String password) {
        if (userRepository.existsByUsername(username))
            return isCorrectPassword(username, password);
        return false;
    }

    private boolean isCorrectPassword(String username, String password) {
        return userRepository.isCorrectPassword(username, password);
    }


    public boolean isAcceptedByAdmin(Long userId) {
        if (userRepository.existsById(userId))
            return userRepository.isAcceptedByUserId(userId).isPresent();
        throw new RuntimeException("User not found by ID: " + userId);
    }


//    //مطمئن هستیم که این یوزر حتما تعریف شده و مقدار نال از دیتابیس نخاهیم گرفت برای همین میتوان از اپشنال هم استفاده نکرد
    public void updateUser(Long userId, User user) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found by ID: " + userId));
        if (isAcceptedByAdmin(userId)) {
            if (user.getFirstname() != null) currentUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null) currentUser.setLastname(user.getLastname());
            if (user.getUsername() != null) currentUser.setUsername(user.getUsername());
            if (user.getPassword() != null) currentUser.setPassword(user.getPassword());
            if (user.getPhoneNumber() != null) currentUser.setPhoneNumber(user.getPhoneNumber());
        } else {
            throw new RuntimeException("user by id " + userId + " is not accepted by admin");
        }
        userRepository.save(currentUser);
    }

    //
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found by ID: " + userId));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found by ID: " + userId));
    }

    public void addAddressById(Long id, Address address) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found by ID: " + id));
        user.setAddress(address);
        userRepository.save(user);
    }

    public void updateAddress(Long userId, Address address) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found by ID: " + userId));
        if (isAcceptedByAdmin(userId)) {
            addressService.updateAddress(user.getAddress().getId(), address);
        } else {
            throw new RuntimeException("user by id " + userId + " is not accepted by admin");
        }
        userRepository.save(user);
    }

    public void acceptUserById(Long adminId, Long userId) {
        if (userRepository.existsById(adminId) && userRepository.existsById(userId) && userRepository.isAdminById(adminId)) {
            userRepository.acceptUserById(userId);
        } else {
            throw new UserNotFoundException("user not found by ID: " + userId + " or admin not found by ID: " + adminId);
        }
    }

    public String signIn(String username, String password) {
        if(isTrueUSer(username, password)) {
            return "welcome"+"user by username "+username;
        }
        throw new UserNotFoundException("user not found by username " + username +" or wrong password by "+password);
    }
}
