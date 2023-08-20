package com.cp.toolrental.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.Chainsaw;

class WeekdayRuleTest {

    WeekdayRule weekdayRule;

    @BeforeEach
    void setup() {
        weekdayRule = new WeekdayRule();
    }

    @ParameterizedTest
    @MethodSource("weekdayChargeCheckoutDateRentalDayCountExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(boolean weekdayCharge, LocalDate checkoutDate, int rentalDayCount,
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
                .weekdayCharge(weekdayCharge)
                .weekendCharge(false)
                .holidayCharge(true)
                .build();
        int chargeDays = weekdayRule.getChargeDay(order, chainsaw, checkoutDate.plusDays(rentalDayCount));

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> weekdayChargeCheckoutDateRentalDayCountExpectedChargeableDays() {
        return Stream.of(
                // weekdayCharge, checkoutDate, rentalDayCount, expectedChargeableDays
                Arguments.of(true, LocalDate.of(2023, 8, 19), 5, 4), // Checkout on a saturday
                Arguments.of(true, LocalDate.of(2023, 8, 20), 5, 5), // Checkout on a sunday
                Arguments.of(false, LocalDate.of(2023, 8, 20), 5, 0)); // Checkout on a monday
    }
}
