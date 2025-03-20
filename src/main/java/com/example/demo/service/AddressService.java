package com.example.demo.service;

import com.example.demo.Exception.AddressNotFoundException;
import com.example.demo.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repository.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public void updateAddress(Long addressId, Address address) {
        Address address1 = addressRepository.findById(addressId).orElseThrow
                (() -> new AddressNotFoundException("Address not found by ID: " + addressId));
        if (address.getCountry() != null)
            address1.setCountry(address.getCountry());
        if (address.getCity() != null)
            address1.setCity(address.getCity());
        if(address.getFloor() != null)
            address1.setFloor(address.getFloor());
        if(address.getStreet() != null)
            address1.setStreet(address.getStreet());
        if(address.getHomeId() != null)
            address1.setHomeId(address.getHomeId());
        addressRepository.save(address1);
    }


}
