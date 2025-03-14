package mortgage.services;

import mortgage.model.InputData;
import mortgage.model.Rate;
import mortgage.model.TimePoint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TimePointCalculationServiceImpl implements TimePointCalculationService {

    public TimePoint calculate(final BigDecimal rateNumber, final InputData inputData) {
        if (!BigDecimal.ONE.equals(rateNumber)) {
            throw new RuntimeException("This method only accepts rateNumber equal to ONE");
        }
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        LocalDate date = inputData.getRepaymentStartDate();
        return new TimePoint(year, month, date);
    }

    public TimePoint calculate(BigDecimal rateNumber, Rate previousRate) {
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        LocalDate date = previousRate.getTimePoint().getDate().plus(1, ChronoUnit.MONTHS);
        return new TimePoint(year, month, date);
    }

    private BigDecimal calculateYear(final BigDecimal rateNumber) {
        return rateNumber.divide(AmountsCalculationService.YEAR, RoundingMode.UP).max(BigDecimal.ONE);
    }

    private BigDecimal calculateMonth(final BigDecimal rateNumber) {
        return BigDecimal.ZERO.equals(rateNumber.remainder(AmountsCalculationService.YEAR))
                ? AmountsCalculationService.YEAR
                : rateNumber.remainder(AmountsCalculationService.YEAR);
    }

}
