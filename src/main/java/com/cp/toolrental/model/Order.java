package com.cp.toolrental.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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

    @NotNull
    @Pattern(regexp = "CHNS|LADW|JAKD|JAKR")
    String toolCode;

    @NotNull
    @Min(1)
    int rentalDayCount;

    @NotNull
    @Min(0)
    @Max(100)
    int discountPercent;

    @NotNull
    LocalDate checkoutDate;
}
