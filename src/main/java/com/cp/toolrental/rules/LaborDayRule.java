package com.cp.toolrental.rules;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static java.time.temporal.TemporalAdjusters.firstInMonth;

/**
 * Determines if today is the first Monday in September (Labor Day).
 */
@Component
public class LaborDayRule implements IDateRule {

    /**
     * Determines if today is the first Monday in September (Labor Day).
     */
    @Override
    public boolean shouldChargeForDay(LocalDate currentDate) {
        LocalDate laborDay = LocalDate.now().withMonth(Month.SEPTEMBER.getValue()).with(firstInMonth(DayOfWeek.MONDAY));
        return currentDate.isEqual(laborDay);
    }
}
