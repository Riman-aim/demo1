package com.example.demo.controller;


import com.example.demo.domain.Address;
import com.example.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/AddressManaging")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;

}
