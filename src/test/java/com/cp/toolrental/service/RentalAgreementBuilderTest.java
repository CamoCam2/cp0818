package com.cp.toolrental.service;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.model.tools.ITool;
import com.cp.toolrental.rules.ChargeDaysRulesEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalAgreementBuilderTest {

    RentalAgreementBuilder rentalAgreementBuilder;

    ObjectMapper objectMapper;

    @Mock
    ChargeDaysRulesEngine mockChargeDaysRulesEngine;

    DecimalFormat decimalFormat;

    @Mock
    Validator mockValidator;

    @BeforeEach
    void setup() {
        decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        this.rentalAgreementBuilder = new RentalAgreementBuilder(mockChargeDaysRulesEngine, decimalFormat, mockValidator);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @ParameterizedTest
    @MethodSource("toolCodeAndType")
    void buildRentalAgreement_shouldSetToolCode(String toolCode) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(toolCode, rentalAgreement.getTool().getToolCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20})
    void buildRentalAgreement_shouldSetRentalDays(int rentalDayCount) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode("TOOL")
                .rentalDayCount(rentalDayCount)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(rentalDayCount, rentalAgreement.getRentalDayCount());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-08-19", "2023-08-20", "2023-08-21"})
    void buildRentalAgreement_shouldSetCheckoutDate(String checkoutDate) {
        LocalDate date = LocalDate.parse(checkoutDate);
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode("TOOL")
                .checkoutDate(date)
                .build());

        assertEquals(date, rentalAgreement.getCheckoutDate());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 15, 20})
    void buildRentalAgreement_shouldSetDiscountPercentage(int discountPercent) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode("TOOL")
                .discountPercent(discountPercent)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(discountPercent, rentalAgreement.getDiscountPercentage());
    }

    @ParameterizedTest
    @MethodSource("toolCodeAndType")
    void buildRentalAgreement_shouldSetToolType(String toolCode, String toolType) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(toolType, rentalAgreement.getTool().getToolType());
    }

    @ParameterizedTest
    @MethodSource("toolCodeAndBrand")
    void buildRentalAgreement_shouldSetToolBrand(String toolCode, String toolBrand) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(toolBrand, rentalAgreement.getTool().getToolBrand());
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 5, 10})
    void buildRentalAgreement_shouldSetDueDate(int dayCount) {
        LocalDate now = LocalDate.now();
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode("CHNS")
                .checkoutDate(now)
                .rentalDayCount(dayCount)
                .build());

        LocalDate expectedDueDate = now.plusDays(dayCount);

        assertEquals(expectedDueDate, rentalAgreement.getDueDate());
    }

    @ParameterizedTest
    @MethodSource("toolCodeAndCost")
    void buildRentalAgreement_shouldSetDailyRentalCharge(String toolCode, Double cost) {
        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(cost, rentalAgreement.getTool().getDailyRentalCharge());
    }

    @Test
    void buildRentalAgreement_shouldSetCountOfChargeableDays() {
        when(mockChargeDaysRulesEngine.getTotalChargeDays(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(4);

        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode("CHNS")
                .checkoutDate(LocalDate.now())
                .rentalDayCount(5)
                .build());

        assertEquals(4, rentalAgreement.getChargeDays());
    }

    @ParameterizedTest
    @MethodSource("toolCodeChargeDaysPreDiscountCharge")
    void buildRentalAgreement_shouldSetPreDiscountCharge(String toolCode, int chargeDays, Double preDiscountCharge) {
        when(mockChargeDaysRulesEngine.getTotalChargeDays(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(chargeDays);

        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .build());

        assertEquals(preDiscountCharge, rentalAgreement.getPreDiscountCharge());
    }

    @ParameterizedTest
    @MethodSource("toolCodeChargeDaysDiscountPercentageDiscountAmount")
    void buildRentalAgreement_shouldSetDiscountPercent(String toolCode, int chargeDays, int discountPercent, Double expectedDiscountAmount) {
        when(mockChargeDaysRulesEngine.getTotalChargeDays(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(chargeDays);

        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .discountPercent(discountPercent)
                .build());

        assertEquals(expectedDiscountAmount, rentalAgreement.getDiscountAmount());
    }

    @ParameterizedTest
    @MethodSource("toolCodeChargeDaysDiscountPercentageFinalCharge")
    void buildRentalAgreement_shouldSetFinalCharge(String toolCode, int chargeDays, int discountPercent, Double expectedFinalCharge) {
        when(mockChargeDaysRulesEngine.getTotalChargeDays(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(chargeDays);

        RentalAgreement rentalAgreement = rentalAgreementBuilder.buildRentalAgreement(Order.builder()
                .toolCode(toolCode)
                .checkoutDate(LocalDate.now())
                .discountPercent(discountPercent)
                .build());

        assertEquals(expectedFinalCharge, rentalAgreement.getFinalCharge());
    }

    private static Stream<Arguments> toolCodeAndType() {
        return Stream.of(
                Arguments.of("CHNS", "Chainsaw"),
                Arguments.of("LADW", "Ladder"),
                Arguments.of("JAKD", "Jackhammer"),
                Arguments.of("JAKR", "Jackhammer"));
    }

    private static Stream<Arguments> toolCodeAndBrand() {
        return Stream.of(
                Arguments.of("CHNS", "Stihl"),
                Arguments.of("LADW", "Werner"),
                Arguments.of("JAKD", "DeWalt"),
                Arguments.of("JAKR", "Ridgid"));
    }

    private static Stream<Arguments> toolCodeAndCost() {
        return Stream.of(
                Arguments.of("CHNS", 1.49),
                Arguments.of("LADW", 1.99),
                Arguments.of("JAKD", 2.99),
                Arguments.of("JAKR", 2.99));
    }

    private static Stream<Arguments> toolCodeChargeDaysPreDiscountCharge() {
        return Stream.of(
                Arguments.of("CHNS", 2, 2.98),
                Arguments.of("LADW", 18, 35.82),
                Arguments.of("JAKD", 7, 20.93));
    }

    private static Stream<Arguments> toolCodeChargeDaysDiscountPercentageDiscountAmount() { // Pre Discount charge * discount percentage
        return Stream.of(
                Arguments.of("CHNS", 2, 20, .60),
                Arguments.of("LADW", 18, 48, 17.19),
                Arguments.of("JAKD", 7, 96, 20.09));
    }

    private static Stream<Arguments> toolCodeChargeDaysDiscountPercentageFinalCharge() { // Pre Discount charge * discount percentage
        return Stream.of(
                Arguments.of("CHNS", 2, 20, 2.38),
                Arguments.of("LADW", 18, 48, 18.63),
                Arguments.of("JAKD", 7, 96, .84));
    }
}
