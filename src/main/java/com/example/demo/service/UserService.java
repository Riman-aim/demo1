package com.example.demo.service;


import com.example.demo.Exception.NotAcceptedUserException;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.convertor.UserConvertor;
import com.example.demo.domain.Address;
import com.example.demo.domain.ExceptionResponse;
import com.example.demo.domain.SelfResponse;
import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserRepository;

import java.util.List;

@Service
public class UserService {


    private UserRepository userRepository;

    private AddressService addressService;

    private UserConvertor userConvertor;

    public UserService() {
    }
    @Autowired
    public UserService(AddressService addressService, UserRepository userRepository, UserConvertor userConvertor) {
        this.addressService = addressService;
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public String getPasswordByUsername(String username) {
        return userRepository.getPasswordByUsername(username).
                orElseThrow(() -> new UserNotFoundException("user not found by username  " + username));
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
        throw new UserNotFoundException("User not found by ID: " + userId);
    }

    public UserDTO updateUser(Long userId, User user) {
        User currentUser = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        if (isAcceptedByAdmin(userId)) {
            if (user.getFirstname() != null)
                currentUser.setFirstname(user.getFirstname());
            if (user.getLastname() != null)
                currentUser.setLastname(user.getLastname());
            if (user.getUsername() != null)
                currentUser.setUsername(user.getUsername());
            if (user.getPassword() != null)
                currentUser.setPassword(user.getPassword());
            if (user.getPhoneNumber() != null)
                currentUser.setPhoneNumber(user.getPhoneNumber());
        } else {
            throw new NotAcceptedUserException("user by id " + userId + " is not accepted by admin");
        }
        userRepository.save(currentUser);
        return userConvertor.userToUserDTO(currentUser);
    }

    public UserDTO findById(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        return userConvertor.userToUserDTO(user);
    }

    public void addAddressById(Long id, Address address) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + id));
        user.setAddress(address);
        userRepository.save(user);
    }

    public void updateAddress(Long userId, Address address) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        if (isAcceptedByAdmin(userId)) {
            addressService.updateAddress(user.getAddress().getId(), address);
        } else {
            throw new NotAcceptedUserException("user by id " + userId + " is not accepted by admin");
        }
        userRepository.save(user);
    }

    public void acceptUserById( Long userId) {
        userRepository.acceptUserById(userId);
    }

    public String signIn(String username, String password) {
        if(isTrueUSer(username, password)) {
            return "welcome"+"user by username "+username;
        }
        throw new UserNotFoundException
                ("user not found by username " + username +" or wrong password by "+password);
    }

    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    public List<User> getNotAcceptedUsers() {
        return userRepository.getNotAcceptedUsers();
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public SelfResponse getSelfResponse(String message , String details) {
        return new SelfResponse(message, details);
    }
    public ExceptionResponse getExceptionResponse(String message, String details) {
        return new ExceptionResponse(message, details);
    }
}
