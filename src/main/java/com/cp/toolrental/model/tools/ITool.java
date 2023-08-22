package com.cp.toolrental.model.tools;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ITool {
    String getToolCode();

    String getToolType();

    String getToolBrand();

    Double getDailyRentalCharge();

    @JsonIgnore
    boolean isWeekdayCharge();

    @JsonIgnore
    boolean isWeekendCharge();

    @JsonIgnore
    boolean isHolidayCharge();

    /**
     * Returns a Stihl Chainsaw.
     *
     * @return {@link Chainsaw}.
     */
    static ITool getChainsaw() {
        return Chainsaw.builder()
                .toolCode("CHNS")
                .toolType("Chainsaw")
                .toolBrand("Stihl")
                .dailyRentalCharge(1.49)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(true)
                .build();
    }

    /**
     * Returns a Werner Ladder.
     *
     * @return {@link Ladder}.
     */
    static ITool getLadder() {
        return Ladder.builder()
                .toolCode("LADW")
                .toolType("Ladder")
                .toolBrand("Werner")
                .dailyRentalCharge(1.99)
                .weekdayCharge(true)
                .weekendCharge(true)
                .holidayCharge(false)
                .build();
    }

    /**
     * Returns a DeWalt Jackhammer.
     *
     * @return {@link Jackhammer}.
     */
    static ITool getDeWaltJackhammer() {
        return Jackhammer.builder()
                .toolCode("JAKD")
                .toolType("Jackhammer")
                .toolBrand("DeWalt")
                .dailyRentalCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build();
    }

    /**
     * Returns a Ridgid Jackhammer.
     *
     * @return {@link Jackhammer}.
     */
    static ITool getRidgidJackhammer() {
        return Jackhammer.builder()
                .toolCode("JAKR")
                .toolType("Jackhammer")
                .toolBrand("Ridgid")
                .dailyRentalCharge(2.99)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(false)
                .build();
    }

    /**
     * Returns an unknown tool if the tool code wasn't recognized.
     *
     * @return {@link Unknown}.
     */
    static ITool getUnknown() {
        return Chainsaw.builder()
                .toolCode("CHNS")
                .toolType("Chainsaw")
                .toolBrand("Stihl")
                .dailyRentalCharge(1.49)
                .weekdayCharge(true)
                .weekendCharge(false)
                .holidayCharge(true)
                .build();
    }

    /**
     * Determines the {@link ITool} based off of the provided tool code.
     *
     * @param toolCode - Used to determine the {@link ITool} type.
     * @return {@link ITool} implementation.
     */
    static ITool determineTool(String toolCode) {
        return switch (toolCode) {
            case "CHNS" -> ITool.getChainsaw();
            case "LADW" -> ITool.getLadder();
            case "JAKD" -> ITool.getDeWaltJackhammer();
            case "JAKR" -> ITool.getRidgidJackhammer();
            default -> ITool.getUnknown();
        };
    }
}
