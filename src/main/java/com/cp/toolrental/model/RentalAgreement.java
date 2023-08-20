package com.cp.toolrental.model;

import java.time.LocalDate;

import com.cp.toolrental.model.tools.ITool;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalAgreement {
    ITool tool;

    int rentalDayCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate checkoutDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dueDate;

    int dailyRentalCharge;

    int chargeDays;

    Double preDiscountCharge;

    int discountPercentage;

    Double discountAmount;

    Double finalCharge;
}
