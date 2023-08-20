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

class WeekendRuleTest {
    WeekendRule weekendRule;

    @BeforeEach
    void setup() {
        weekendRule = new WeekendRule();
    }

    @ParameterizedTest
    @MethodSource("weekendChargeCheckoutDateRentalDayCountExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(boolean weekendCharge, LocalDate checkoutDate, int rentalDayCount,
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
                .weekendCharge(weekendCharge)
                .holidayCharge(true)
                .build();
        int chargeDays = weekendRule.getChargeDay(order, chainsaw, checkoutDate.plusDays(rentalDayCount));

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> weekendChargeCheckoutDateRentalDayCountExpectedChargeableDays() {
        return Stream.of(
                // weekendCharge, checkoutDate, rentalDayCount, expectedChargeableDays
                Arguments.of(true, LocalDate.of(2023, 8, 19), 5, 1), // Checkout on a saturday
                Arguments.of(true, LocalDate.of(2023, 8, 20), 5, 0), // Checkout on a sunday
                Arguments.of(true, LocalDate.of(2023, 8, 18), 14, 4), // Checkout on a friday
                Arguments.of(false, LocalDate.of(2023, 8, 20), 5, 0)); // Checkout on a sunday
    }
}
