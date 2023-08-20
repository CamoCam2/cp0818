package com.cp.toolrental.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.Chainsaw;

class FourthOfJulyRuleTest {

    FourthOfJulyRule fourthOfJulyRule;

    @BeforeEach
    void setup() {
        fourthOfJulyRule = new FourthOfJulyRule();
    }

    @ParameterizedTest
    @MethodSource("holidayChargeCheckoutDateRentalDayCountExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(boolean holidayCharge, LocalDate checkoutDate, int rentalDayCount,
            int expectedChargeableDays) {
        Order order = Order.builder()
                .checkoutDate(checkoutDate)
                .rentalDayCount(rentalDayCount)
                .build();

        Chainsaw chainsaw = Chainsaw.builder()
                .toolCode("CHNS")
                .toolType("Chainsaw")
                .toolBrand("Stihl")
                .dailyCharge(1.49)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(holidayCharge)
                .build();
        int chargeDays = fourthOfJulyRule.getChargeDay(order, chainsaw, checkoutDate.plusDays(rentalDayCount));

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> holidayChargeCheckoutDateRentalDayCountExpectedChargeableDays() {
        return Stream.of(
                // holidayCharge, checkoutDate, rentalDayCount, expectedChargeableDays
                Arguments.of(true, LocalDate.of(2023, 7, 3), 5, 1), // Checkout on a monday, 4th on tuesday
                Arguments.of(true, LocalDate.of(2026, 7, 3), 5, 0), // Checkout on a friday, 4th on saturday (observed friday)
                Arguments.of(true, LocalDate.of(2027, 7, 3), 1, 0), // Checkout on a saturday, 4th on sunday (observed monday)
                Arguments.of(true, LocalDate.of(2033, 7, 3), 1, 1), // Checkout on a sunday, 4th on monday
                Arguments.of(false, LocalDate.of(2023, 7, 3), 5, 0)); // Checkout on a monday, 4th on tuesday
    }
}
