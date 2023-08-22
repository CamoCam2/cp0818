package com.cp.toolrental.rules;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.tools.ITool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class ChargeDaysRulesEngine {

    @Autowired
    private List<IDateRule> rules;

    /**
     * Calculates total number of days to charge the customer.
     *
     * @param order   - {@link Order}.
     * @param tool    - {@link ITool}.
     * @param dueDate - Date the tool is due to be returned.
     * @return number of days to charge.
     */
    public int getTotalChargeDays(Order order, ITool tool, LocalDate dueDate) {
        log.info("Calculating total charge days...");
        // Start our rental period the day after checkout
        LocalDate rentalDate = order.getCheckoutDate().plusDays(1);
        int chargeDayCounter = 0;

        // loop through all days until we pass our due date
        while (!rentalDate.isAfter(dueDate)) {
            LocalDate currentDate = rentalDate; // Need this since I use the rentalDate in the stream
            Set<Class<? extends IDateRule>> ruleClassSet = rules.stream()
                    // check each rule to see if it applies
                    .map(rule -> Pair.with(rule, rule.shouldChargeForDay(currentDate)))
                    // filter out rules that don't apply
                    .filter(Pair::getValue1)
                    .map(Pair::getValue0)
                    // extract the type of rule that was applied
                    .map(IDateRule::getClass)
                    .collect(Collectors.toSet());

            log.debug("Rule(s) applied on {}: {}", rentalDate, ruleClassSet);

            chargeDayCounter += chargeDayIfApplicable(ruleClassSet, tool);

            rentalDate = rentalDate.plusDays(1);
        }

        log.info("Returning total charge days: {}", chargeDayCounter);
        return chargeDayCounter;
    }

    /**
     * Analyzes applied rules and determines if we should charge for the day.
     *
     * @param ruleClassSet - List of applied {@link IDateRule}
     * @param tool         - {@link ITool} in the order.
     * @return 1 if we are to charge for the day, otherwise 0;
     */
    private static int chargeDayIfApplicable(Set<Class<? extends IDateRule>> ruleClassSet, ITool tool) {
        // if our set contains the rule, then it was applied
        boolean isWeekday = ruleClassSet.contains(WeekdayRule.class);
        boolean isWeekend = ruleClassSet.contains(WeekendRule.class);
        boolean isFourthOfJuly = ruleClassSet.contains(FourthOfJulyRule.class);
        boolean isLaborDay = ruleClassSet.contains(LaborDayRule.class);

        if ((isWeekday || isWeekend) && isFourthOfJuly) {
            return tool.isHolidayCharge() ? 1 : 0;
        }

        if ((isWeekday || isWeekend) && isLaborDay) {
            return tool.isHolidayCharge() ? 1 : 0;
        }

        if (isWeekday && tool.isWeekdayCharge()) {
            return 1;
        }

        if (isWeekend && tool.isWeekendCharge()) {
            return 1;
        }

        return 0;
    }
}