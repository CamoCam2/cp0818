package com.cp.toolrental.rules;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ChargeDaysRulesEngine {

    @Autowired
    private List<IDateRule> rules;

    public int getTotalChargeDays(Order order, ITool tool, LocalDate dueDate) {
        return rules.stream()
                .map(rule -> rule.getChargeDay(order, tool, dueDate))
                .collect(Collectors.toList())
                .stream()
                .reduce(0, (a, b) -> a + b);
    }
}