package com.example.demo.convertor;


import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDtoShow;
import com.example.demo.dto.userdto.UserGetRequest;
import com.example.demo.dto.userdto.UserSaveResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {


    private final ModelMapper modelMapper;

    @Autowired
    public UserConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDtoShow userToUserDTO(User user) {
        return modelMapper.map(user, UserDtoShow.class);
    }

    public User userDtoToUser(UserDtoShow userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserSaveResponse UserToUserSaveResponse(User user) {
        return modelMapper.map(user, UserSaveResponse.class);
    }

    public UserGetRequest UserToUserGetRequest(User user) {
        return modelMapper.map(user, UserGetRequest.class);
    }


}
