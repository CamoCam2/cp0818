package com.cp.toolrental.integration;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.model.tools.Chainsaw;
import com.cp.toolrental.model.tools.Jackhammer;
import com.cp.toolrental.model.tools.Ladder;
import com.cp.toolrental.rules.*;
import com.cp.toolrental.service.RentalAgreementBuilder;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class IntegrationTest {

    RentalAgreementBuilder rentalAgreementBuilder;

    ChargeDaysRulesEngine chargeDaysRulesEngine;

    DecimalFormat decimalFormat;

    @Autowired
    Validator validator;

    @BeforeEach
    void setup() {
        decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        chargeDaysRulesEngine = new ChargeDaysRulesEngine(Arrays.asList(new WeekdayRule(), new WeekendRule(), new FourthOfJulyRule(), new LaborDayRule()));
        rentalAgreementBuilder = new RentalAgreementBuilder(chargeDaysRulesEngine, decimalFormat, validator);
    }

    @ParameterizedTest
    @MethodSource({"order"})
    void shouldProduceTheCorrectRentalAgreement(Order order, RentalAgreement expectedRentalAgreement) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(order);
        assertEquals(expectedRentalAgreement, rentalAgreement);
    }

    @Test
    void shouldThrowExceptionForInvalidDiscount() {
        assertThrows(ConstraintViolationException.class, () -> rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .discountPercent(101)
                .rentalDayCount(5)
                .toolCode("JAKR")
                .checkoutDate(LocalDate.of(2015, Month.SEPTEMBER, 3))
                .build()));
    }

    private static Stream<Arguments> order() {
        // ---------------------------
        Order order1 = Order.builder()
                .toolCode("LADW")
                .checkoutDate(LocalDate.of(2020, Month.JULY, 2))
                .rentalDayCount(3)
                .discountPercent(10)
                .build();
        RentalAgreement rentalAgreement1 = RentalAgreement.builder()
                .tool(Ladder.builder()
                        .toolCode("LADW")
                        .toolType("Ladder")
                        .toolBrand("Werner")
                        .dailyRentalCharge(1.99)
                        .weekdayCharge(true)
                        .weekendCharge(true)
                        .holidayCharge(false)
                        .build())
                .rentalDayCount(order1.getRentalDayCount())
                .checkoutDate(order1.getCheckoutDate())
                .discountPercentage(order1.getDiscountPercent())
                .dueDate(LocalDate.of(2020, Month.JULY, 5))
                .chargeDays(2)
                .preDiscountCharge(3.98)
                .discountAmount(.40)
                .finalCharge(3.58)
                .build();

        // ---------------------------
        Order order2 = Order.builder()
                .toolCode("CHNS")
                .checkoutDate(LocalDate.of(2015, Month.JULY, 2))
                .rentalDayCount(5)
                .discountPercent(25)
                .build();
        RentalAgreement rentalAgreement2 = RentalAgreement.builder()
                .tool(Chainsaw.builder()
                        .toolCode("CHNS")
                        .toolType("Chainsaw")
                        .toolBrand("Stihl")
                        .dailyRentalCharge(1.49)
                        .weekdayCharge(true)
                        .weekendCharge(false)
                        .holidayCharge(true)
                        .build())
                .rentalDayCount(order2.getRentalDayCount())
                .checkoutDate(order2.getCheckoutDate())
                .discountPercentage(order2.getDiscountPercent())
                .dueDate(LocalDate.of(2015, Month.JULY, 7))
                .chargeDays(3)
                .preDiscountCharge(4.47)
                .discountAmount(1.12)
                .finalCharge(3.35)
                .build();

        // ---------------------------
        Order order3 = Order.builder()
                .toolCode("JAKD")
                .checkoutDate(LocalDate.of(2015, Month.SEPTEMBER, 3))
                .rentalDayCount(6)
                .discountPercent(0)
                .build();
        RentalAgreement rentalAgreement3 = RentalAgreement.builder()
                .tool(Jackhammer.builder()
                        .toolCode("JAKD")
                        .toolType("Jackhammer")
                        .toolBrand("DeWalt")
                        .dailyRentalCharge(2.99)
                        .weekdayCharge(true)
                        .weekendCharge(false)
                        .holidayCharge(false)
                        .build())
                .rentalDayCount(order3.getRentalDayCount())
                .checkoutDate(order3.getCheckoutDate())
                .discountPercentage(order3.getDiscountPercent())
                .dueDate(LocalDate.of(2015, Month.SEPTEMBER, 9))
                .chargeDays(4)
                .preDiscountCharge(11.96)
                .discountAmount(0.0)
                .finalCharge(11.96)
                .build();

        // ---------------------------
        Order order4 = Order.builder()
                .toolCode("JAKR")
                .checkoutDate(LocalDate.of(2015, Month.JULY, 2))
                .rentalDayCount(9)
                .discountPercent(0)
                .build();
        RentalAgreement rentalAgreement4 = RentalAgreement.builder()
                .tool(Jackhammer.builder()
                        .toolCode("JAKR")
                        .toolType("Jackhammer")
                        .toolBrand("Ridgid")
                        .dailyRentalCharge(2.99)
                        .weekdayCharge(true)
                        .weekendCharge(false)
                        .holidayCharge(false)
                        .build())
                .rentalDayCount(order4.getRentalDayCount())
                .checkoutDate(order4.getCheckoutDate())
                .discountPercentage(order4.getDiscountPercent())
                .dueDate(LocalDate.of(2015, Month.JULY, 11))
                .chargeDays(5)
                .preDiscountCharge(14.95)
                .discountAmount(0.0)
                .finalCharge(14.95)
                .build();

        // ---------------------------
        Order order5 = Order.builder()
                .toolCode("JAKR")
                .checkoutDate(LocalDate.of(2020, Month.JULY, 2))
                .rentalDayCount(4)
                .discountPercent(50)
                .build();
        RentalAgreement rentalAgreement5 = RentalAgreement.builder()
                .tool(Jackhammer.builder()
                        .toolCode("JAKR")
                        .toolType("Jackhammer")
                        .toolBrand("Ridgid")
                        .dailyRentalCharge(2.99)
                        .weekdayCharge(true)
                        .weekendCharge(false)
                        .holidayCharge(false)
                        .build())
                .rentalDayCount(order5.getRentalDayCount())
                .checkoutDate(order5.getCheckoutDate())
                .discountPercentage(order5.getDiscountPercent())
                .dueDate(LocalDate.of(2020, Month.JULY, 6))
                .chargeDays(1)
                .preDiscountCharge(2.99)
                .discountAmount(1.50)
                .finalCharge(1.49)
                .build();

        return Stream.of(
                Arguments.of(order1, rentalAgreement1),
                Arguments.of(order2, rentalAgreement2),
                Arguments.of(order3, rentalAgreement3),
                Arguments.of(order4, rentalAgreement4),
                Arguments.of(order5, rentalAgreement5)
        );
    }
}
