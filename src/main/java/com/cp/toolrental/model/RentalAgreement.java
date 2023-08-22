package com.cp.toolrental.model;

import com.cp.toolrental.model.tools.ITool;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RentalAgreement {
    ITool tool;

    int rentalDayCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    LocalDate checkoutDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    LocalDate dueDate;

    int chargeDays;

    Double preDiscountCharge;

    int discountPercentage;

    Double discountAmount;

    Double finalCharge;
}
