package com.cp.toolrental.rules;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;

@Component
public class WeekdayRule implements IDateRule {

    @Override
    public int getChargeDay(Order order, ITool tool, LocalDate dueDate) {
        LocalDate rentalDate = order.getCheckoutDate().plusDays(1);
        int chargeDayCounter = 0;
        while (!rentalDate.isAfter(dueDate)) {
            if (rentalDate.getDayOfWeek().getValue() < 6 && tool.isWeekdayCharge()) {
                chargeDayCounter++;
            }
            rentalDate = rentalDate.plusDays(1);
        }
        return chargeDayCounter;
    }

}
