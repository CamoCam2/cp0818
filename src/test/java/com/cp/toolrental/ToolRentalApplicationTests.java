package com.cp.toolrental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ToolRentalApplicationTests {

    ToolRentalApplication toolRentalApplication;

    @BeforeEach
    void setup() {
        toolRentalApplication = new ToolRentalApplication();
    }

    @Test
    void contextLoads() {
        assertNotNull(toolRentalApplication);
    }

}
