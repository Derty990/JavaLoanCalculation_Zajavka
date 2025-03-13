package mortgage.services;

import mortgage.model.InputData;
import mortgage.model.Rate;
import mortgage.model.RateAmounts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class ConstantAmountsCalculationServiceImplTest {

    private ConstantAmountsCalculationService constantAmountsCalculationService;

    @BeforeEach
    public void setup() {
        this.constantAmountsCalculationService = new ConstantAmountsCalculationServiceImpl();
    }

    @Test
    @DisplayName("Calculate rate amounts for first rate")
    void shouldCalculateFirstRateAmountCorrectly() {
        //given
        InputData inputData = TestData.someInputData();
        RateAmounts expected = TestData.someRateAmounts();

        //when
        RateAmounts result = constantAmountsCalculationService.calculate(inputData, null);

        //then
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Calculate rate amounts for other rates")
    void shouldCalculateOtherRateAmountCorrectly() {
        //given
        InputData inputData = TestData.someInputData();
        Rate rate = TestData.someRate();
        RateAmounts expected = TestData.someRateAmounts()
                .withRateAmount(new BigDecimal("3303.45"))
                .withInterestAmount(new BigDecimal("2483.87"))
                .withCapitalAmount(new BigDecimal("819.58"));

        //when
        RateAmounts result = constantAmountsCalculationService.calculate(inputData, null, rate);

        //then
        Assertions.assertEquals(expected, result);
    }
}