package com.cp.toolrental.rules;

import com.cp.toolrental.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FourthOfJulyRuleTest {

    FourthOfJulyRule fourthOfJulyRule;

    @BeforeEach
    void setup() {
        fourthOfJulyRule = new FourthOfJulyRule();
    }

    @ParameterizedTest
    @MethodSource("checkoutDateExpectedChargeableDays")
    void getChargeDay_shouldReturnCorrectNumberOfDays(LocalDate checkoutDate, int expectedChargeableDays) {
        Order order = Order.builder()
                .checkoutDate(checkoutDate)
                .build();

        int chargeDays = fourthOfJulyRule.shouldChargeForDay(order.getCheckoutDate()) ? 1 : 0;

        assertEquals(expectedChargeableDays, chargeDays);
    }

    private static Stream<Arguments> checkoutDateExpectedChargeableDays() {
        return Stream.of(
                // holidayCharge, checkoutDate, expectedChargeableDays
                Arguments.of(LocalDate.of(2023, 7, 4), 1), // 4th on tuesday
                Arguments.of(LocalDate.of(2026, 7, 4), 0), // 4th on saturday (observed friday)
                Arguments.of(LocalDate.of(2027, 7, 4), 0), // 4th on sunday (observed monday)
                Arguments.of(LocalDate.of(2033, 7, 4), 1), // 4th on monday
                Arguments.of(LocalDate.of(2023, 8, 24), 0)); // Not fourth of July
    }
}
