package com.example.demo.convertor;


import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO userToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User userDTOToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }



}
