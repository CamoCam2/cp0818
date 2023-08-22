package com.cp.toolrental.rules;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface IDateRule {

    /**
     * Determines if we should charge for the day depending on various implementation conditions.
     *
     * @param currentDate - Current {@link LocalDate}.
     * @return true if we should charge for the day, otherwise false.
     */
    boolean shouldChargeForDay(LocalDate currentDate);
}