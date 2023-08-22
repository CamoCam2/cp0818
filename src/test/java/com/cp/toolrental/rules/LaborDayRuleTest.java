package com.cp.toolrental.rules;

import com.cp.toolrental.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaborDayRuleTest {

    LaborDayRule laborDayRule;

    @BeforeEach
    void setup() {
        laborDayRule = new LaborDayRule();
    }

    @ParameterizedTest
    @MethodSource("checkoutDateExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(LocalDate checkoutDate, int expectedChargeableDays) {
        Order order = Order.builder()
                .checkoutDate(checkoutDate)
                .build();
        int chargeDays = laborDayRule.shouldChargeForDay(order.getCheckoutDate()) ? 1 : 0;

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> checkoutDateExpectedChargeableDays() {
        return Stream.of(
                // checkoutDate, expectedChargeableDays
                Arguments.of(LocalDate.of(2023, 9, 4), 1), // Checkout on a labor day monday
                Arguments.of(LocalDate.of(2023, 9, 20), 0)); // Checkout on a random sunday
    }
}