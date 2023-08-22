package com.cp.toolrental.service;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.model.tools.ITool;
import com.cp.toolrental.rules.ChargeDaysRulesEngine;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
@Validated
public class RentalAgreementBuilder {

    @Autowired
    ChargeDaysRulesEngine chargeDaysRulesEngine;

    @Autowired
    DecimalFormat decimalFormat;

    @Autowired
    private Validator validator;

    /**
     * Sets all fields on a rental agreement based off of information provided in the {@link Order}.
     *
     * @param order - {@link Order}.
     * @return {@link RentalAgreement}.
     */
    public RentalAgreement buildRentalAgreement(Order order) {
        validateOrder(order);

        ITool tool = ITool.determineTool(order.getToolCode());
        log.info("Tool determined to be a: {} {}", tool.getToolBrand(), tool.getToolType());
        LocalDate dueDate = determineDueDate(order.getCheckoutDate(), order.getRentalDayCount());
        log.info("The tool will be due: {}", dueDate);
        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .tool(tool)
                .rentalDayCount(order.getRentalDayCount())
                .checkoutDate(order.getCheckoutDate())
                .discountPercentage(order.getDiscountPercent())
                .dueDate(dueDate)
                .chargeDays(chargeDaysRulesEngine.getTotalChargeDays(order, tool, dueDate))
                .build();

        rentalAgreement.setPreDiscountCharge(calculatePreDiscountCharge(rentalAgreement));
        rentalAgreement.setDiscountAmount(calculateDiscountAmount(order, rentalAgreement));
        rentalAgreement.setFinalCharge(calculateFinalCharge(rentalAgreement));
        log.debug("Rental agreement: {}", rentalAgreement);
        return rentalAgreement;
    }

    private void validateOrder(Order order) {
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Order> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException(sb.toString(), violations);
        }
    }

    private double calculateFinalCharge(RentalAgreement rentalAgreement) {
        return Double.parseDouble(decimalFormat.format(rentalAgreement.getPreDiscountCharge() - rentalAgreement.getDiscountAmount()));
    }

    private double calculateDiscountAmount(Order order, RentalAgreement rentalAgreement) {
        return Double.parseDouble(decimalFormat.format(rentalAgreement.getPreDiscountCharge() * (order.getDiscountPercent() / 100.0)));
    }

    private double calculatePreDiscountCharge(RentalAgreement rentalAgreement) {
        return Double.parseDouble(decimalFormat.format(rentalAgreement.getChargeDays() * rentalAgreement.getTool().getDailyRentalCharge()));
    }

    private LocalDate determineDueDate(LocalDate checkoutDate, int rentalDayCount) {
        return checkoutDate
                .plusDays(rentalDayCount);
    }
}
