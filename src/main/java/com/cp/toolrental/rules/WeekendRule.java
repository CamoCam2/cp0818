package com.cp.toolrental.rules;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Determines if it's a weekend.
 */
@Component
public class WeekendRule implements IDateRule {

    /**
     * Determines if it's a weekend.
     */
    @Override
    public boolean shouldChargeForDay(LocalDate currentDate) {
        return currentDate.getDayOfWeek().getValue() > 5;
    }
}