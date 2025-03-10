package mortgage.services;

import mortgage.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class TimePointCalculationServiceImplTest {

    private TimePointCalculationService timePointCalculationService;

    @BeforeEach
    public void setup() {
        this.timePointCalculationService = new TimePointCalculationServiceImpl();
    }

    @Test
    @DisplayName("Should calculate first rate time point successfully")
    void calculateTimePointForFirstRate() {
        //given
        InputData inputData = someInputData();
        TimePoint expected = someTimePoint();

        //when
        TimePoint result = timePointCalculationService.calculate(BigDecimal.valueOf(1), inputData);

        //then
        Assertions.assertEquals(expected, result);

    }

    @Test
    @DisplayName("Should calculate other rate time point than first successfully")
    void calculateTimePointForOtherRates() {
        //given
        TimePoint timePoint = someTimePoint()
                .withYear(BigDecimal.valueOf(2))
                .withMonth(BigDecimal.valueOf(3))
                .withDate(LocalDate.of(2010, 1, 1));

        Rate rate = someRate()
                .withTimePoint(timePoint);

        TimePoint expected = timePoint
                .withDate(timePoint.getDate().plus(1, ChronoUnit.MONTHS));


        //when
        TimePoint result = timePointCalculationService.calculate(BigDecimal.valueOf(15), rate);

        //then
        Assertions.assertEquals(expected, result);
    }

    private static Rate someRate() {
        return Rate.builder()
                .timePoint(someTimePoint())
                .build();
    }

    public static InputData someInputData() {
        return InputData.builder()
                .repaymentStartDate(LocalDate.of(2010, 5, 10))
                .wiborPercent(BigDecimal.valueOf(2.70))
                .amount(BigDecimal.valueOf(198267.46))
                .monthsDuration(BigDecimal.valueOf(180))
                .rateType(MortgageType.CONSTANT)
                .marginPercent(BigDecimal.valueOf(1.8))
                .overpaymentProvisionPercent(BigDecimal.valueOf(3))
                .overpaymentProvisionMonths(BigDecimal.valueOf(36))
                .overpaymentStartMonth(BigDecimal.valueOf(1))
                .overpaymentReduceWay(Overpayment.REDUCE_PERIOD)
                .mortgagePrintPayoffsSchedule(true)
                .mortgageRateNumberToPrint(1)
                .build();

    }

    private static TimePoint someTimePoint() {
        return TimePoint
                .builder()
                .year(BigDecimal.valueOf(1))
                .month(BigDecimal.valueOf(1))
                .date(LocalDate.of(2010, 5, 10))
                .build();
    }

}