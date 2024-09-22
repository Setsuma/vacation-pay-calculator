package ru.neoflex.vacation_pay_calculator.service;

import org.springframework.stereotype.Service;
import ru.neoflex.vacation_pay_calculator.dto.request.VacationPayRequestDto;
import ru.neoflex.vacation_pay_calculator.dto.response.VacationPayResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VacationPayServiceImpl implements VacationPayService {
    /** Среднее количество дней в месяце без учета федеральных праздников */
    private static final double AVERAGE_NUMBER_DAYS_IN_MONTH = 29.3;
    /** Процент НДФЛ */
    private static final double NDFL_PERCENT = 0.13;

    @Override
    public VacationPayResponseDto calculateVacationPay(VacationPayRequestDto vacationPayRequestDto) {
        int paidDays;
        if (vacationPayRequestDto.getVacationStartDate() == null)
            paidDays = vacationPayRequestDto.getVacationDays();
        else
            paidDays = calculatePaidDays(vacationPayRequestDto.getVacationStartDate(),
                    vacationPayRequestDto.getVacationDays());

        BigDecimal vacationPay = vacationPayRequestDto.getAverageSalary()
                .divide(BigDecimal.valueOf(AVERAGE_NUMBER_DAYS_IN_MONTH), RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(paidDays));

        BigDecimal amountNDFL = vacationPay
                .multiply(BigDecimal.valueOf(NDFL_PERCENT))
                .setScale(0, RoundingMode.HALF_UP);

        BigDecimal vacationPayWithNDFLSubtract = vacationPay.subtract(amountNDFL);

        //Производится расчет с учетом НДФЛ
        //Должна ли также возвращаться сумма до вычета НДФЛ и его размер я бы уточнил
        return new VacationPayResponseDto("Отпускные с вычетом НДФЛ", vacationPayWithNDFLSubtract);
    }

    static int calculatePaidDays(LocalDate startDate, int vacationDays) {
        List holidayDays = getHolidays();

        //Обычно расчет введется по календарным дням без вычета выходных
        //А праздничные дни переносятся, либо вычитаются из количества дней отпуска
        //Поэтому я бы этот момент уточнил
        List<LocalDate> listPaidVacationDates =
                Stream.iterate(startDate, nextVacationDate -> nextVacationDate.plusDays(1)).limit(vacationDays)
                        .filter(vacationDate -> !(holidayDays.contains(MonthDay.from(vacationDate))))
                        .filter(vacationDate -> !(vacationDate.getDayOfWeek() == DayOfWeek.SATURDAY
                                                 || vacationDate.getDayOfWeek() == DayOfWeek.SUNDAY))
                        .collect(Collectors.toList());

        return listPaidVacationDates.size();
    }

    static List<MonthDay> getHolidays() {
        return Collections.unmodifiableList(Stream.of(
                MonthDay.of(1, 1),
                MonthDay.of(1, 2),
                MonthDay.of(1, 3),
                MonthDay.of(1, 4),
                MonthDay.of(1, 5),
                MonthDay.of(1, 6),
                MonthDay.of(1, 7),
                MonthDay.of(1, 8),
                MonthDay.of(2, 23),
                MonthDay.of(3, 8),
                MonthDay.of(5, 1),
                MonthDay.of(5, 9),
                MonthDay.of(6, 12),
                MonthDay.of(11, 4)
        ).collect(Collectors.toList()));
    }
}