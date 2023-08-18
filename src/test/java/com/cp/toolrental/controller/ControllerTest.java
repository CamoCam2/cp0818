package com.cp.toolrental.controller;

import java.util.Date;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.cp.toolrental.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@WebMvcTest
@AutoConfigureMockMvc
@Slf4j
public class ControllerTest {

    @Autowired
    Controller controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkout_shouldFailValidation_rentalDayCount() throws JsonProcessingException, Exception {
        Order order = Order.builder()
                .checkoutDate(new Date())
                .rentalDayCount(0)
                .discountPercent(0)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                .content(new ObjectMapper().writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rentalDayCount", Is.is("must be greater than or equal to 1")));
    }
}
