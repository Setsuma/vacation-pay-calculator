package ru.neoflex.vacation_pay_calculator.service;

import org.junit.jupiter.api.Test;
import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VacationPayServiceImplTest {

    private final VacationPayService service = new VacationPayServiceImpl();

    // Тест без даты
    @Test
    void calculateVacationPay_withoutStartDate_shouldCalculateCorrectly() {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), 10, null);

        VacationPayResponseDto response = service.calculateVacationPay(request);

        assertEquals(new BigDecimal("29693"), response.getVacationPay());
    }

    // Тест c датой, в которой нет праздничного дня
    @Test
    void calculateVacationPay_withStartDate_shouldCalculateCorrectly() {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), 10, LocalDate.of(2025, 1, 15));

        VacationPayResponseDto response = service.calculateVacationPay(request);

        assertEquals(new BigDecimal("23754"), response.getVacationPay());
    }

    // Тест c датой, в которой есть праздничные дниь
    @Test
    void calculateVacationPay_withHolidays_shouldCalculateCorrectly() {
        VacationPayRequestDto request =
                new VacationPayRequestDto(new BigDecimal("100000"), 10, LocalDate.of(2025, 5, 1));

        VacationPayResponseDto response = service.calculateVacationPay(request);

        assertEquals(new BigDecimal("14847"), response.getVacationPay());
    }

    // Тест расчета неоплачиваемых дней
    @Test
    void calculatePaidDays_shouldExcludeWeekendsAndHolidays() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        int vacationDays = 10;

        int paidDays = VacationPayServiceImpl.calculatePaidDays(startDate, vacationDays);

        assertEquals(5, paidDays); // Ожидается 5 оплачиваемых дней (исключая 1 мая, 9 мая и выходные)
    }
}