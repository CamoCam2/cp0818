package com.cp.toolrental.controller;

import com.cp.toolrental.model.Order;
import com.cp.toolrental.model.RentalAgreement;
import com.cp.toolrental.rules.ChargeDaysRulesEngine;
import com.cp.toolrental.rules.WeekdayRule;
import com.cp.toolrental.service.RentalAgreementBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
@AutoConfigureMockMvc
@Import({RentalAgreementBuilder.class, ChargeDaysRulesEngine.class, WeekdayRule.class, DecimalFormat.class})
public class ControllerTest {

    @Autowired
    Controller controller;


    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    class CheckoutValidationFailed {

        @ParameterizedTest
        @ValueSource(strings = {"HEY", "WRONG", "nope"})
        void checkout_shouldFailValidation_toolCode(String toolCode)
                throws Exception {
            Order order = Order.builder()
                    .checkoutDate(LocalDate.now())
                    .toolCode(toolCode)
                    .discountPercent(0)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.toolCode",
                            Is.is("must match \"CHNS|LADW|JAKD|JAKR\"")));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -10})
        void checkout_shouldFailValidation_rentalDayCount(int rentalDayCount)
                throws Exception {
            Order order = Order.builder()
                    .checkoutDate(LocalDate.now())
                    .rentalDayCount(rentalDayCount)
                    .discountPercent(0)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.rentalDayCount",
                            Is.is("must be greater than or equal to 1")));
        }

        @ParameterizedTest
        @MethodSource("provideParameters")
        void checkout_shouldFailValidation_discountPercent(int discountPercent, String message)
                throws Exception {
            Order order = Order.builder()
                    .checkoutDate(LocalDate.now())
                    .rentalDayCount(10)
                    .discountPercent(discountPercent)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.discountPercent", Is.is(message)));
        }

        @Test
        void checkout_shouldFailValidation_nullToolCode()
                throws Exception {
            Order order = Order.builder()
                    .checkoutDate(LocalDate.now())
                    .rentalDayCount(10)
                    .discountPercent(50)
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.toolCode",
                            Is.is("must not be null")));
        }

        @Test
        void checkout_shouldFailValidation_nullRentalDayCount()
                throws Exception {
            Order order = Order.builder()
                    .checkoutDate(LocalDate.now())
                    .discountPercent(50)
                    .toolCode("CHNS")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.rentalDayCount",
                            Is.is("must be greater than or equal to 1")));
        }

        @Test
        void checkout_shouldFailValidation_nullCheckoutDate()
                throws Exception {

            Order order = Order.builder()
                    .discountPercent(50)
                    .toolCode("CHNS")
                    .build();

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(objectMapper.writeValueAsString(order))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.checkoutDate",
                            Is.is("must not be null")));

        }

        @Test
        void checkout_shouldFailValidation_invalidDateFormat()
                throws Exception {

            String json = "{\"rentalDayCount\": 50, \"discountPercent\": 50, \"toolCode\": \"TEST\", \"checkoutDate\": \"123\"}";

            mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Is.is("Date must match format: mm/dd/yy")));

        }

        private static Stream<Arguments> provideParameters() {
            return Stream.of(
                    Arguments.of(101, "must be less than or equal to 100"),
                    Arguments.of(-10, "must be greater than or equal to 0"));
        }
    }
}
