package com.cp.toolrental.model;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalAgreement {
    Tool tool;
    List<Date> rentalDays;
    Date checkoutDate;
    Date dueDate;
    int dailyRentalCharge;
    int chargeDays;
    Double preDiscountCharge;
    int discountPercentage;
    Double discountAmount;
    Double finalCharge;
}
