package ru.neoflex.vacation_pay_calculator.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VacationPayRequestDto {
    @NotNull(message = "Average salary for 12 months cannot be empty")
    @Positive(message = "Average salary for 12 months must be greater than 0")
    private BigDecimal averageSalary;

    @NotNull(message = "Vacation days cannot be empty")
    @Positive(message = "Vacation days must be greater than 0")
    private Integer vacationDays;

    @Nullable
    private LocalDate vacationStartDate;
}