package com.cp.toolrental.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Charge {
    Double dailyCharge;
    boolean weekdayCharge;
    boolean weekendCharge;
    boolean holidayCharge;
}
