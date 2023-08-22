package com.cp.toolrental.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Configuration
public class Config {

    /**
     * Provides an {@link ObjectMapper} as a Bean.
     *
     * @return {@link ObjectMapper}.
     */
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * Provides a {@link DecimalFormat} with rounding mode set to HALF_UP as a Bean.
     *
     * @return {@link DecimalFormat}.
     */
    @Bean
    DecimalFormat decimalFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat;
    }
}
