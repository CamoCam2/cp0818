package com.cp.toolrental.rules;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;

@Component
public interface IDateRule {
    int getChargeDay(Order order, ITool tool, LocalDate dueDate);
}

// RULES
// Is weekday and tool has weekday charge
// Is weekend and tool has weekend charge
// Is 4th of July and tool has holiday charge
// Is Labor Day and tool has holiday charge
