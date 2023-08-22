package com.cp.toolrental.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yy")
    LocalDate checkoutDate;
}
