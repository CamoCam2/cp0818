package com.cp.toolrental.rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;

@ExtendWith(MockitoExtension.class)
class ChargeDaysRulesEngineTest {
    ChargeDaysRulesEngine chargeDaysRulesEngine;

    @Mock
    WeekdayRule mockWeekdayRule;

    @Mock
    WeekendRule mockWeekendRule;

    @BeforeEach
    void setup() {
        this.chargeDaysRulesEngine = new ChargeDaysRulesEngine(Arrays.asList(mockWeekdayRule, mockWeekendRule));
    }

    @Test
    void getTotalChargeDays_shouldCallRules() {
        chargeDaysRulesEngine.getTotalChargeDays(Order.builder().build(), ITool.getChainsaw(), LocalDate.now());
        verify(mockWeekdayRule).getChargeDay(any(Order.class), any(ITool.class), any(LocalDate.class));
        verify(mockWeekendRule).getChargeDay(any(Order.class), any(ITool.class), any(LocalDate.class));
    }

    @Test
    void getTotalChargeDays_shouldReturnSumOfChargeDays() {
        when(mockWeekdayRule.getChargeDay(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(3);
        when(mockWeekendRule.getChargeDay(any(Order.class), any(ITool.class), any(LocalDate.class))).thenReturn(4);
        int chargeDays = chargeDaysRulesEngine.getTotalChargeDays(Order.builder().build(), ITool.getChainsaw(), LocalDate.now());
        assertEquals(7, chargeDays);
    }
}
