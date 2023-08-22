package com.cp.toolrental.rules;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Determines if it's a weekday.
 */
@Component
public class WeekdayRule implements IDateRule {

    /**
     * Determines if it's a weekday.
     */
    @Override
    public boolean shouldChargeForDay(LocalDate currentDate) {
        return currentDate.getDayOfWeek().getValue() < 6;
    }
}
