package com.cp.toolrental.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.model.tools.ITool;
import com.cp.toolrental.rules.ChargeDaysRulesEngine;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RentalAgreementBuilder {

    @Autowired
    ChargeDaysRulesEngine chargeDaysRulesEngine;

    public RentalAgreement buildRentalAgreement(Order order) {
        ITool tool = ITool.determineTool(order.getToolCode());
        LocalDate dueDate = determineDueDate(order.getCheckoutDate(), order.getRentalDayCount());
        return RentalAgreement.builder()
                .tool(tool)
                .rentalDayCount(order.getRentalDayCount())
                .checkoutDate(order.getCheckoutDate())
                .discountPercentage(order.getDiscountPercent())
                .dueDate(dueDate)
                .chargeDays(chargeDaysRulesEngine.getTotalChargeDays(order, tool, dueDate))
                .build();
    }

    // private int determineChargeDays(Order order, ITool tool, LocalDate dueDate) {
    //     int chargeDayCounter = 0;
    //     LocalDate rentalDate = order.getCheckoutDate().plusDays(1);

    //     while (!rentalDate.isAfter(dueDate)) {
    //         if (rentalDate.getDayOfWeek().getValue() > 5 && tool.isWeekendCharge()) {
    //             chargeDayCounter++;
    //         } else if (rentalDate.getDayOfWeek().getValue() < 6 && tool.isWeekdayCharge()) {
    //             chargeDayCounter++;
    //         }
    //         rentalDate = rentalDate.plusDays(1);
    //     }

    //     return chargeDayCounter;
    // }

    private LocalDate determineDueDate(LocalDate checkoutDate, int rentalDayCount) {
        return checkoutDate
                .plusDays(rentalDayCount);
    }
}
