package com.cp.toolrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.service.RentalAgreementBuilder;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class Controller {

    @Autowired
    RentalAgreementBuilder rentalAgreementBuilder;

    @PostMapping("/checkout")
    public RentalAgreement checkout(@RequestBody @Valid Order order) {
        return rentalAgreementBuilder.buildRentalAgreement(order);
    }

}