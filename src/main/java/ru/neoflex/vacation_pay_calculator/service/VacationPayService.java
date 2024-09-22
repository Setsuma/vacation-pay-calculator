package ru.neoflex.vacation_pay_calculator.service;

import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;

public interface VacationPayService {

    VacationPayResponseDto calculateVacationPay(VacationPayRequestDto vacationPayRequestDto);
}