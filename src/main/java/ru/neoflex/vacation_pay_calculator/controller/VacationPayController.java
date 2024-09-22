package ru.neoflex.vacation_pay_calculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;
import ru.neoflex.vacation_pay_calculator.service.VacationPayService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class VacationPayController {
    private final VacationPayService vacationPayService;

    // Думаю эндпоинт должен называться /calculate, поэтому уточнил бы этот момент
    @GetMapping("/calculacte")
    public ResponseEntity<VacationPayResponseDto> getVacationPay(@Valid @RequestBody VacationPayRequestDto request) {
        return ResponseEntity.ok(vacationPayService.calculateVacationPay(request));
    }
}