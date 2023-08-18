package com.cp.toolrental.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Tool {
    String toolCode;
    String toolType;
    String toolBrand;
    Charge charge;
}
