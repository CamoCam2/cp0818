package com.cp.toolrental.controller;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.service.RentalAgreementBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class Controller {

    @Autowired
    RentalAgreementBuilder rentalAgreementBuilder;

    /**
     * REST endpoint to trigger the tool rental checkout process.
     *
     * @param order - {@link Order}
     * @return {@link RentalAgreement}
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/checkout")
    public RentalAgreement checkout(@RequestBody @Valid Order order) {
        log.debug("Received checkout request for: {}", order.getToolCode());
        log.debug("Full Order: {}", order);
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(order);
        log.info("Tool code: {}", rentalAgreement.getTool().getToolCode());
        log.info("Tool type: {}", rentalAgreement.getTool().getToolType());
        log.info("...");
        log.info("Final charge: ${}", rentalAgreement.getFinalCharge());
        log.info("\n");
        return rentalAgreement;
    }

}