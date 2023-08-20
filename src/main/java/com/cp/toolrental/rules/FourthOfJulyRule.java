package com.cp.toolrental.rules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import org.springframework.stereotype.Component;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;

@Component
public class FourthOfJulyRule implements IDateRule {

    @Override
    public int getChargeDay(Order order, ITool tool, LocalDate dueDate) {
        if (!tool.isHolidayCharge()) {
            return 0;
        }

        LocalDate rentalDate = order.getCheckoutDate().plusDays(1);
        while (!rentalDate.isAfter(dueDate)) {
            // If it's the day before the 4th and it's a Friday, the 4th will be observed today
            if (rentalDate.getMonth() == Month.JULY
                    && rentalDate.getDayOfMonth() == 3
                    && rentalDate.getDayOfWeek() == DayOfWeek.FRIDAY
                    && tool.isHolidayCharge()) {
                return 1;
            }

            // If it's the day after the 4th and it's a Monday, the 4th will be observed today
            if (rentalDate.getMonth() == Month.JULY
                    && rentalDate.getDayOfMonth() == 5
                    && rentalDate.getDayOfWeek() == DayOfWeek.MONDAY
                    && tool.isHolidayCharge()) {
                return 1;
            }

            if (rentalDate.getMonth() == Month.JULY
                    && rentalDate.getDayOfMonth() == 4
                    && rentalDate.getDayOfWeek().getValue() < 6
                    && tool.isHolidayCharge()) {
                return 1;
            }
            rentalDate = rentalDate.plusDays(1);
        }
        return 0;
    }

}
