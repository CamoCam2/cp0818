package com.cp.toolrental.rules;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.Chainsaw;
import com.cp.toolrental.model.tools.ITool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChargeDaysRulesEngineTest {
    ChargeDaysRulesEngine chargeDaysRulesEngine;

    @Mock
    WeekdayRule mockWeekdayRule;

    @Mock
    WeekendRule mockWeekendRule;

    @Mock
    FourthOfJulyRule mockFourthOfJulyRule;

    @Mock
    LaborDayRule mockLaborDayRule;

    @BeforeEach
    void setup() {
        this.chargeDaysRulesEngine = new ChargeDaysRulesEngine(asList(mockWeekdayRule, mockWeekendRule, mockFourthOfJulyRule, mockLaborDayRule));
    }

    @Test
    void getTotalChargeDays_shouldCallRules() {
        chargeDaysRulesEngine.getTotalChargeDays(Order.builder().checkoutDate(LocalDate.now().minusDays(1)).build(), ITool.getChainsaw(), LocalDate.now());
        verify(mockWeekdayRule).shouldChargeForDay(any(LocalDate.class));
        verify(mockWeekendRule).shouldChargeForDay(any(LocalDate.class));
        verify(mockFourthOfJulyRule).shouldChargeForDay(any(LocalDate.class));
    }

    @Test
    void getTotalChargeDays_shouldChargeOnWeekday() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);

        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder()
                .checkoutDate(LocalDate.now().minusDays(1))
                .build(), ITool.getChainsaw(), LocalDate.now());
        assertEquals(1, chargeDays);
    }

    @Test
    void getTotalChargeDays_shouldChargeOnWeekend() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);

        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder()
                .checkoutDate(LocalDate.now().minusDays(1))
                .build(), ITool.getChainsaw(), LocalDate.now());
        assertEquals(1, chargeDays);
    }

    // If it's a weekday, but it's also a holiday, and they have no holiday charge, do not charge them
    @Test
    void getTotalChargeDays_shouldNotChargeOnWeekdayFourthOfJulyNoHolidayCharge() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true).thenReturn(true).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false).thenReturn(false).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true).thenReturn(false).thenReturn(false);

        Order order = Order
                .builder()
                .checkoutDate(LocalDate.of(2023, Month.JULY, 3))
                .rentalDayCount(3)
                .build();
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(order, Chainsaw.builder().weekdayCharge(true).holidayCharge(false).build(), order.getCheckoutDate().plusDays(order.getRentalDayCount()));
        assertEquals(2, chargeDays);
    }

    // If it's a weekday, but it's also a holiday, and they have a holiday charge, charge them
    @Test
    void getTotalChargeDays_shouldChargeOnWeekdayFourthOfJuly() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder()
                .checkoutDate(LocalDate.now().minusDays(1))
                .build(), Chainsaw.builder().weekdayCharge(true).holidayCharge(true).build(), LocalDate.now());
        assertEquals(1, chargeDays);
    }

    // If it's a weekend, but it's also the 4th of July, and they have a holiday charge, and they have weekend charge, charge them
    @Test
    void getTotalChargeDays_shouldChargeWhenWeekendFourthOfJulyWithHolidayChargeAndWeekendCharge() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder().checkoutDate(LocalDate.now().minusDays(1))
                .build(), Chainsaw.builder().weekdayCharge(true).holidayCharge(true).build(), LocalDate.now());
        assertEquals(1, chargeDays);
    }

    // If it's a weekend, but it's also the 4th of July, and they don't have a holiday charge, and they don't have a weekend charge, don't charge for the weekend or the weekday
    @Test
    void getTotalChargeDays_shouldNotChargeOnWeekendFourthOfJulyWithNoHolidayOrWeekendCharge() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder().checkoutDate(LocalDate.now().minusDays(1))
                .build(), Chainsaw.builder().weekdayCharge(true).weekendCharge(false).holidayCharge(false).build(), LocalDate.now());
        assertEquals(0, chargeDays);
    }

    @Test
    void getTotalChargeDays_shouldNotChargeOnWeekdayLaborDayWithNoHolidayCharge() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockLaborDayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder().checkoutDate(LocalDate.now().minusDays(1))
                .build(), Chainsaw.builder().weekdayCharge(true).holidayCharge(false).build(), LocalDate.now());
        assertEquals(0, chargeDays);
    }

    @Test
    void getTotalChargeDays_shouldChargeOnWeekdayLaborDayWithHolidayCharge() {
        when(mockWeekdayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        when(mockWeekendRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockFourthOfJulyRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(false);
        when(mockLaborDayRule.shouldChargeForDay(any(LocalDate.class))).thenReturn(true);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order
                .builder().checkoutDate(LocalDate.now().minusDays(1))
                .build(), Chainsaw.builder().weekdayCharge(true).holidayCharge(true).build(), LocalDate.now());
        assertEquals(1, chargeDays);
    }
}
