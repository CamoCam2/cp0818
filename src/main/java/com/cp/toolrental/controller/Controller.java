package com.cp.toolrental.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;

import jakarta.validation.Valid;

@RestController
public class Controller {

    @PostMapping("/checkout")
    public RentalAgreement checkout(@RequestBody @Valid Order order) {
        return null;
    }

}