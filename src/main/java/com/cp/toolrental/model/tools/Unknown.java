package com.cp.toolrental.model.tools;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Unknown implements ITool {
    String toolCode;
    String toolType;
    String toolBrand; 
    Double dailyCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;   
}
