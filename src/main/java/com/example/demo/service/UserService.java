package com.example.demo.service;


import com.example.demo.dto.userdto.UserGetRequest;
import com.example.demo.dto.userdto.UserSaveRequest;
import com.example.demo.dto.userdto.UserSaveResponse;
import com.example.demo.exception.*;
import com.example.demo.convertor.UserConvertor;
import com.example.demo.domain.Address;
import com.example.demo.dto.response.ExceptionResponse;
import com.example.demo.dto.response.SelfResponse;
import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDtoShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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


    public UserSaveResponse save(UserSaveRequest user) {

        if (!userRepository.isDuplicateUsername(user.getUsername())) {
            if (!userRepository.isDuplicatePhoneNumber(user.getPhoneNumber())) {
                User user1=userConvertor.UserSaveRequestToUSer(user);
                user1.setAddressDeleted(false);
                user1.setAccepted(false);
                user1.setAdmin(false);
                userRepository.save(user1);
                return userConvertor.UserToUserSaveResponse(user1);
            } else {
                throw new DuplicatePhoneNumberException("phone number is already taken by phone number " + user.getPhoneNumber());
            }
        } else {
            throw new DuplicateUsernameException("user name is already taken by user name " + user.getUsername());
        }


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

    //this method must not throw exception
    public boolean isAcceptedByAdmin(Long userId) {
        if (userRepository.existsById(userId))
            return userRepository.isAcceptedByUserId(userId);
//        throw new UserNotFoundException("User not found by ID: " + userId);
        return false;
    }

    public UserDtoShow updateUser(Long userId, User user) {
        User currentUser = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        if (!userRepository.isDuplicateUsername(user.getUsername())) {
            if (!userRepository.isDuplicatePhoneNumber(user.getPhoneNumber())) {
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
            } else {
                throw new DuplicatePhoneNumberException("phone number is already taken by phone number " + user.getPhoneNumber());
            }
        } else {
            throw new DuplicateUsernameException("user name is already taken by user name " + user.getUsername());

        }
        userRepository.save(currentUser);
        return userConvertor.userToUserDTO(currentUser);
    }

    public UserGetRequest findById(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        return userConvertor.UserToUserGetRequest(user);
    }

    public void addAddressById(Long id, Address address) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + id));
        if(user.getAddress().getId()==null){
            user.setAddress(address);
            userRepository.save(user);
        }
        else {
            throw new AddressAlreadyExistException("address is already initialized by address id " + address.getId());
        }
    }

    public void updateAddress(Long userId, Address address) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
        if(user.getAddress().getId()!=0){
            if (isAcceptedByAdmin(userId)) {
                addressService.updateAddress(user.getAddress().getId(), address);
            } else {
                throw new NotAcceptedUserException("user by id " + userId + " is not accepted by admin");
            }
            userRepository.save(user);
        }
        else {
            throw new AddressNotInitialized("address is not initialized yet");
        }
    }

    public void acceptUserById(Long userId) {
        userRepository.acceptUserById(userId);
    }

    public void signIn(String username, String password) {
        if (isTrueUSer(username, password)) {
            return;
        }
        throw new UserNotFoundException
                ("user not found by username " + username + " or wrong password by " + password);
    }

    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    public List<User> getNotAcceptedUsers() {
        return userRepository.getNotAcceptedUsers();
    }

    public void deleteUserById(Long userId) {
        if(userRepository.existsById(userId)){
            if(userRepository.isAcceptedByUserId(userId)){
                userRepository.deleteById(userId);
            }
            else {
                throw new NotAcceptedUserException("user by id " + userId + " is not accepted by admin");
            }
        }
        else {
            throw new UserNotFoundException("user not found by ID: " + userId);
        }
    }

    public void deleteAddressByUserId(Long userId) {
        if (userRepository.existsById(userId)) {
            if (userRepository.isAcceptedByUserId(userId)) {
                User user = userRepository.findById(userId).
                        orElseThrow(() -> new UserNotFoundException("User not found by ID: " + userId));
                if (!user.isAddressDeleted()) {
                    user.setAddressDeleted(true);
                    userRepository.save(user);
                } else {
                    throw new FieldCantBeChanged("you already do not have an address to delete ");
                }
            } else {
                throw new NotAcceptedUserException("user by id " + userId + " is not accepted by admin");
            }
        } else {
            throw new UserNotFoundException("User not found by ID: " + userId);
        }
    }

    public SelfResponse getSelfResponse(String message, String details) {
        return new SelfResponse(message, details);
    }

    public ExceptionResponse getExceptionResponse(String message, String details) {
        return new ExceptionResponse(message, details);
    }

    public List<User> getDeAddressedUsers() {
        return userRepository.getAddressDeletedUsers();
    }


    @Scheduled(cron = "0 32 2 * * *")
    public void deleteAddressDeletedUsers() {
        List<User> users = userRepository.getAddressDeletedUsers();
        userRepository.deleteAll(users);
    }

    public List<User> getAddressDeletedUsers() {
        return userRepository.getAddressDeletedUsers();
    }

}
