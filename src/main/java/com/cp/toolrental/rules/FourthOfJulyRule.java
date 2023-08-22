package com.cp.toolrental.rules;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Determines if it's the 4th of July, or if the 4th lands on a weekend, the Friday before or Monday after.
 */
@Component
public class FourthOfJulyRule implements IDateRule {

    /**
     * Determines if it's the 4th of July, or if the 4th lands on a weekend, the Friday before or Monday after.
     */
    @Override
    public boolean shouldChargeForDay(LocalDate currentDate) {
        if (currentDate.getMonth() == Month.JULY) {
            // If it's the day before the 4th, and it's a Friday, charge for today
            if (currentDate.getDayOfMonth() == 3
                    && currentDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
                return true;
            }

            // If it's the Monday after the 4th, charge for today
            if (currentDate.getDayOfMonth() == 5
                    && currentDate.getDayOfWeek() == DayOfWeek.MONDAY) {
                return true;
            }

            // If today is the 4th of July on a weekday
            return currentDate.getDayOfMonth() == 4
                    && currentDate.getDayOfWeek().getValue() < 6;
        } else {
            return false;
        }
    }
}
