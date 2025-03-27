package com.example.demo.service;

import com.example.demo.exception.AddressNotFoundException;
import com.example.demo.domain.Address;
import com.example.demo.repository.AddressRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    Address address ;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(addressRepository);
        address = new Address();
        address.setCity("city");
        address.setCountry("country");
        address.setStreet("street");
        address.setFloor("floor");
        address.setId(1L);
        address.setHomeId("homeId");
    }

    @Nested
    @DisplayName("update address ")
    class updateAddress {

        @Test
        @DisplayName("exception -> address id not found ")
        public void addressIdNotFound() {
            when(addressRepository.findById(1L)).thenThrow(AddressNotFoundException.class);
            Assertions.assertThrows(AddressNotFoundException.class,()->addressService.updateAddress(1L,address));
        }

        @Test
        @DisplayName("ok -> update address completed ")
        public void updateAddressCompleted() {
            when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
            addressService.updateAddress(1L,address);
        }
    }
}