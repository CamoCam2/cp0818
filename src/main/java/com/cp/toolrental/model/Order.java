package com.cp.toolrental.model;

import java.util.Date;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Valid
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Order {
    String toolCode;

    @Min(1)
    int rentalDayCount;

    @Min(0)
    @Max(100)
    int discountPercent;

    Date checkoutDate;
}
