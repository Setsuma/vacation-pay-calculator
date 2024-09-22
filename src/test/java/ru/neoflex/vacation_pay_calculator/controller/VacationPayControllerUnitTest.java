package ru.neoflex.vacation_pay_calculator.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;
import ru.neoflex.vacation_pay_calculator.service.VacationPayService;
import ru.neoflex.vacation_pay_calculator.service.VacationPayServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VacationPayControllerUnitTest {
    private VacationPayService mockVacationPayService;
    private VacationPayController vacationPayController;
    private VacationPayRequestDto request;
    private VacationPayResponseDto response;

    @BeforeAll
    void setUp() {
        mockVacationPayService = Mockito.mock(VacationPayServiceImpl.class);
        vacationPayController = new VacationPayController(mockVacationPayService);
        request = new VacationPayRequestDto(BigDecimal.valueOf(100000), 10, null);
        response = new VacationPayResponseDto("Отпускные с вычетом НДФЛ", new BigDecimal("29693"));
    }

    @Test
    void getVacationPayTest() {
        when(mockVacationPayService.calculateVacationPay(any(VacationPayRequestDto.class))).thenReturn(response);
        assertEquals(response, vacationPayController.getVacationPay(request).getBody());
    }
}