package com.cp.toolrental.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigTest {

    Config config;

    @BeforeEach
    void setup() {
        config = new Config();
    }

    @Test
    void objectMapper() {
        assertNotNull(config.objectMapper());
    }

    @Test
    void decimalFormat() {
        DecimalFormat decimalFormat = config.decimalFormat();
        assertEquals(RoundingMode.HALF_UP, decimalFormat.getRoundingMode());
    }
}