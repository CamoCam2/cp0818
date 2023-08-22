package com.cp.toolrental.model.tools;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Unknown implements ITool {
    String toolCode;
    String toolType;
    String toolBrand;
    Double dailyRentalCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;
}
