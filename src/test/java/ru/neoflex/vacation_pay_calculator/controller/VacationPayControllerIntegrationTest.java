package ru.neoflex.vacation_pay_calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;
import ru.neoflex.vacation_pay_calculator.service.VacationPayService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VacationPayController.class)
public class VacationPayControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationPayService service;

    @Autowired
    private ObjectMapper objectMapper;

    // Тест без даты
    @Test
    void testGetVacationPayWithoutStartDate() throws Exception {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), 10, null);
        VacationPayResponseDto response =
                new VacationPayResponseDto("Отпускные с вычетом НДФЛ", new BigDecimal("29693"));

        when(service.calculateVacationPay(request)).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculacte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("Отпускные с вычетом НДФЛ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(response.getVacationPay().toString()));
    }

    // Тест c датой
    @Test
    void testGetVacationPayWithStartDate() throws Exception {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), 10, LocalDate.of(2025, 5, 1));
        VacationPayResponseDto response =
                new VacationPayResponseDto("Отпускные с вычетом НДФЛ", new BigDecimal("14847"));

        when(service.calculateVacationPay(request)).thenReturn(response);

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculacte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("Отпускные с вычетом НДФЛ"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.vacationPay").value(response.getVacationPay().toString()));
    }

    // Тест с неверными параметрами (пустой средней заработной платы)
    @Test
    void testGetVacationPayWithInvalidAverageSalary() throws Exception {
        VacationPayRequestDto request = new VacationPayRequestDto(null, 10, LocalDate.of(2025, 1, 15));

        mockMvc.perform(MockMvcRequestBuilders.get("/calculacte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists());
    }

    // Тест с неверными параметрами (отрицательное количество дней отпуска)
    @Test
    void testGetVacationPayWithInvalidVacationDays() throws Exception {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), -10, LocalDate.of(2025, 1, 15));

        mockMvc.perform(MockMvcRequestBuilders.get("/calculacte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists());
    }
}