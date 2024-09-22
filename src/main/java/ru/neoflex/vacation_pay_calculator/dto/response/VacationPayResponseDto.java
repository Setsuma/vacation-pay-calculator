package ru.neoflex.vacation_pay_calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VacationPayResponseDto {
    private String responseMessage;
    private BigDecimal vacationPay;
}