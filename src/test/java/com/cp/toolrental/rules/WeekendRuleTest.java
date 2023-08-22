package com.cp.toolrental.rules;

import com.cp.toolrental.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeekendRuleTest {
    WeekendRule weekendRule;

    @BeforeEach
    void setup() {
        weekendRule = new WeekendRule();
    }

    @ParameterizedTest
    @MethodSource("checkoutDateExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(LocalDate checkoutDate, int expectedChargeableDays) {
        Order order = Order.builder()
                .checkoutDate(checkoutDate)
                .build();

        int chargeDays = weekendRule.shouldChargeForDay(order.getCheckoutDate()) ? 1 : 0;

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> checkoutDateExpectedChargeableDays() {
        return Stream.of(
                // checkoutDate, expectedChargeableDays
                Arguments.of(LocalDate.of(2023, 8, 19), 1), // Checkout on a saturday
                Arguments.of(LocalDate.of(2023, 8, 21), 0)); // Checkout on a sunday
    }
}
