package com.example.demo.controller;


import com.example.demo.domain.Address;
import com.example.demo.dto.response.SelfResponse;
import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDtoShow;
import com.example.demo.dto.userdto.UserGetRequest;
import com.example.demo.dto.userdto.UserSaveRequest;
import com.example.demo.dto.userdto.UserSaveResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UserSaveResponse saveUser( @Valid @RequestBody UserSaveRequest user) {
        return userService.save(user) ;
    }

    @GetMapping("/getPassword/{username}")
    @ResponseStatus(HttpStatus.OK)
    public String getPassword(@PathVariable String username) {
        return userService.getPasswordByUsername(username);
    }

    @GetMapping("/getAllInfo/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetRequest getAllInfo(@PathVariable Long userId) {
        return(userService.findById(userId));
    }

    @PatchMapping("/updateFields/{id}")
    public UserDtoShow updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PatchMapping("/addAddress/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public SelfResponse addAddress(@PathVariable Long id, @RequestBody Address address) {
        userService.addAddressById(id, address);
        return new SelfResponse("address added by userID : " + id, "success");

    }

    @PatchMapping("/updateAddress/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public SelfResponse updateAddress(@PathVariable Long userId, @RequestBody Address address) {
        userService.updateAddress(userId, address);
        return (new SelfResponse("address updated by userID : " + userId, "success"));
    }

    @GetMapping("/signIn/{username}/{password}")
    @ResponseStatus(HttpStatus.OK)
    public SelfResponse signIn(@PathVariable String username, @PathVariable String password) {
        userService.signIn(username, password);
        return (new SelfResponse("welcome user by username : " + username, "success"));
    }

    @DeleteMapping("/deleteAccount/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SelfResponse deleteAccount(@PathVariable Long id) {
        userService.deleteUserById(id);
        return (new SelfResponse("account deleted", "success"));
    }

    @PatchMapping("/deleteAddress/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SelfResponse deleteAddress(@PathVariable Long id) {
        userService.deleteAddressByUserId(id);
        return(new SelfResponse("address deleted", "success"));
    }


}
