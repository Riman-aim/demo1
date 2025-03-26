package com.example.demo.service;

import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.exception.NotAcceptedUserException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.convertor.UserConvertor;
import com.example.demo.domain.User;
import com.example.demo.dto.userdto.UserDtoShow;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AddressService addressService;

    @Mock
    ModelMapper modelMapper;

    @Mock
    UserConvertor userConvertor;

    @InjectMocks
    UserService userService;

    private com.example.demo.domain.Address address;

    private User user;


    @BeforeEach
    public void init() {
        userConvertor = new UserConvertor(modelMapper);
        userService = new UserService(addressService, userRepository, userConvertor);
//        addressService =  new AddressService(addressRepository);
//for update address because of mocking

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstname("firstname");
        user.setLastname("lastname");
        user.setPhoneNumber("phoneNumber");
        user.setAccepted(false);
        user.setAdmin(false);

        address = new com.example.demo.domain.Address();
        address.setCity("city");
        address.setCountry("country");
        address.setStreet("street");
        address.setFloor("floor");
        address.setId(1L);
        address.setHomeId("homeId");

        user.setAddress(address);

    }


    @Nested
    class isCorrectPassword {

        @Test
        @DisplayName("ok -> user name exist and pass")
        public void getPassword_Exist() {
            when(userRepository.getPasswordByUsername("username")).thenReturn(Optional.of("password"));
            Assertions.assertEquals("password", userService.getPasswordByUsername("username"));
        }

        @Test
        @DisplayName("exception -> when user name not exist")
        public void getPassword_NotExist() {
            when(userRepository.getPasswordByUsername("username")).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> userService.getPasswordByUsername("username"));
        }
    }

    @Nested
    class isTrueUser {
        @Test
        @DisplayName("false -> user name not exist ")
        public void usernameNotExist() {
            when(userRepository.existsByUsername("username")).thenReturn(false);
            assert !userService.isTrueUSer("username", "password");
        }

        @Test
        @DisplayName("false -> password is wrong for correct user name")
        public void passwordIsWrong() {
            when(userRepository.existsByUsername("username")).thenReturn(true);
            when(userRepository.isCorrectPassword("username", "password")).thenReturn(false);
            assert !userService.isTrueUSer("username", "password");
        }

        @Test
        @DisplayName("true -> is a true user ")
        public void istrueUser() {
            when(userRepository.existsByUsername("username")).thenReturn(true);
            when(userRepository.isCorrectPassword("username", "password")).thenReturn(true);
            assert userService.isTrueUSer("username", "password");
        }

    }

    @Nested
    class acceptedByAdmin {
        @Test
        @DisplayName("false -> userId not found")
        public void notExistById() {
            when(userRepository.existsById(1L)).thenReturn(false);
            assert !userService.isAcceptedByAdmin(1L);
        }

        @Test
        @DisplayName("false -> not accepted by admin")
        public void notAcceptedByAdmin() {
            when(userRepository.existsById(1L)).thenReturn(true);
            when(userRepository.isAcceptedByUserId(1L)).thenReturn(false);
            assert !userService.isAcceptedByAdmin(1L);
        }

        @Test
        @DisplayName("true -> accepted by admin")
        public void acceptByAdmin() {
            when(userRepository.existsById(1L)).thenReturn(true);
            when(userRepository.isAcceptedByUserId(1L)).thenReturn(true);
            assert userService.isAcceptedByAdmin(1L);
        }
    }

    @Nested
    class findById {
        @Test
        @DisplayName("exception -> wrong id")
        public void wrongId() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> userService.findById(1L));

        }

        @Test
        @DisplayName("ok -> userDtoShow")
        public void userDtoShow() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            UserDtoShow convertedUser = userConvertor.userToUserDTO(user);
            UserDtoShow expectedUserDtoShow = userService.findById(1L);
            Assertions.assertEquals(userService.findById(1L), convertedUser);
        }
    }

    @Nested
    class Address {
        @Test
        @DisplayName("exception -> id not found")
        public void idNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> userService.addAddressById(1L, address));
        }

        @Test
        @DisplayName("ok -> add successfully ")
        public void addSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            userService.addAddressById(1L, address);
            verify(userRepository).save(user);
        }

    }

    @Nested
    class updateAddress {
        @Test
        @DisplayName("exception -> user not found ")
        public void userNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateAddress(1L, address));
        }

        @Test
        @DisplayName("exception -> not accepted user ")
        public void notAcceptedUser() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.existsById(1L)).thenReturn(true);
            when(userRepository.isAcceptedByUserId(1L)).thenReturn(false);
            Assertions.assertThrows(NotAcceptedUserException.class, () -> userService.updateAddress(1L, address));
        }

        @Test
        @DisplayName("exception -> no address found for this user ")
        public void addressIdNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
            when(userRepository.existsById(1L)).thenReturn(true);
            when(userRepository.isAcceptedByUserId(1L)).thenReturn(true);
            doThrow(AddressNotFoundException.class).when(addressService).updateAddress(1L, address);
            Assertions.assertThrows(AddressNotFoundException.class, () -> userService.updateAddress(1L, address));
        }

        @Test
        @DisplayName("successfully added ")
        public void addSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.existsById(1L)).thenReturn(true);
            when(userRepository.isAcceptedByUserId(1L)).thenReturn(true);
            userService.updateAddress(1L, address);
            verify(userRepository).save(user);
        }

    }

    @Nested
    @DisplayName("find by id ")
    class findByIdById {
        @Test
        @DisplayName("exception -> user not found by id ")
        public void userNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        }

        @Test
        @DisplayName("ok -> true user id ")
        public void trueUserId() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            UserDtoShow convertedUser = userConvertor.userToUserDTO(user);
            Assertions.assertEquals(userService.findById(1L), convertedUser);
        }

    }

    @Nested
    @DisplayName("add address by id")
    public class addAddressById {
        @Test
        @DisplayName("id not found ")
        public void idNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.addAddressById(1L, address));
        }

        @Test
        @DisplayName("successful")
        public void addSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            userService.addAddressById(1L, address);
            verify(userRepository).save(user);
        }

    }

    @Nested
    @DisplayName("sign in ")
    public class signIn {

        @Test
        @DisplayName("exception -> user not found ")
        public void userNotFound() {
            when(userRepository.existsByUsername("username")).thenReturn(false);
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signIn("username", "password"));
        }

        @Test
        @DisplayName("exception -> password wrong ")
        public void passwordWrong() {
            when(userRepository.existsByUsername("username")).thenReturn(true);
            when(userRepository.isCorrectPassword("username", "password")).thenReturn(false);
            Assertions.assertThrows(UserNotFoundException.class, () -> userService.signIn("username", "password"));
        }

        @Test
        @DisplayName("successful ")
        public void signInSuccessfully() {
            when(userRepository.existsByUsername("username")).thenReturn(true);
            when(userRepository.isCorrectPassword("username", "password")).thenReturn(true);
            Assertions.assertDoesNotThrow(() -> userService.signIn("username", "password"));
        }
    }

}
