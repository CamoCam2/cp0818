package com.cp.toolrental.model.tools;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ITool {
    String getToolCode();

    String getToolType();

    String getToolBrand();

    Double getDailyCharge();

    @JsonIgnore
    boolean isWeekdayCharge();

    @JsonIgnore
    boolean isWeekendCharge();

    @JsonIgnore
    boolean isHolidayCharge();

    static ITool getChainsaw() {
        return Chainsaw.builder()
        .toolCode("CHNS")
        .toolType("Chainsaw")
        .toolBrand("Stihl")
        .dailyCharge(1.49)
        .weekdayCharge(true)
        .weekendCharge(false)
        .holidayCharge(true)
        .build();
    }

    static ITool getLadder() {
        return Ladder.builder()
        .toolCode("LADW")
        .toolType("Ladder")
        .toolBrand("Werner")
        .dailyCharge(1.99)
        .weekdayCharge(true)
        .weekendCharge(true)
        .holidayCharge(false)
        .build();
    }

    static ITool getDeWaltJackhammer() {
        return Jackhammer.builder()
        .toolCode("JAKD")
        .toolType("Jackhammer")
        .toolBrand("DeWalt")
        .dailyCharge(2.99)
        .weekdayCharge(true)
        .weekendCharge(false)
        .holidayCharge(false)
        .build();
    }

    static ITool getRidgidJackhammer() {
        return Jackhammer.builder()
        .toolCode("JAKR")
        .toolType("Jackhammer")
        .toolBrand("Ridgid")
        .dailyCharge(2.99)
        .weekdayCharge(true)
        .weekendCharge(false)
        .holidayCharge(false)
        .build();
    }

    static ITool getUnknown() {
        return Chainsaw.builder()
        .toolCode("CHNS")
        .toolType("Chainsaw")
        .toolBrand("Stihl")
        .dailyCharge(1.49)
        .weekdayCharge(true)
        .weekendCharge(false)
        .holidayCharge(true)
        .build();
    }

    static ITool determineTool(String toolCode) {
        switch (toolCode) {
            case "CHNS":
                return ITool.getChainsaw();
            case "LADW":
                return ITool.getLadder();
            case "JAKD":
                return ITool.getDeWaltJackhammer();
            case "JAKR":
                return ITool.getRidgidJackhammer();
            default:
                return ITool.getUnknown();
        }
    }
}
