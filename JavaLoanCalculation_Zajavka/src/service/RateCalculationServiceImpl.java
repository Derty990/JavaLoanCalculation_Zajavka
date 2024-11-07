package service;

import model.*;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class RateCalculationServiceImpl implements RateCalculationService {

    private final TimePointService timePointService;

    private final AmountsCalculationService amountsCalculationServie;

    private final ResidualCalculationService residualCalculationService;

    public RateCalculationServiceImpl(
            TimePointService timePointService,
            AmountsCalculationService amountsCalculationServie,
            ResidualCalculationService residualCalculationService)
    {
        this.timePointService = timePointService;
        this.amountsCalculationServie = amountsCalculationServie;
        this.residualCalculationService = residualCalculationService;
    }

    @Override
    public List<Rate> calculate(InputData inputData) {
        List<Rate> rates = new LinkedList<>();

        BigDecimal rateNumber = BigDecimal.ONE;

        Rate firstRate = calculateRate(rateNumber, inputData);
        rates.add(firstRate);

        Rate previousRate = firstRate;

        for(BigDecimal index = rateNumber.add(BigDecimal.ONE);
            index.compareTo(inputData.getMonthsDuration()) <= 0;
            index = index.add(BigDecimal.ONE))
        {
            Rate nextRate = calculateRate(index, inputData, previousRate);
            rates.add(nextRate);
            previousRate = nextRate;
        }

        return rates;

    }

    private Rate calculateRate(BigDecimal rateNumber, InputData inputData) {

        TimePoint timePoint = timePointService.calculate(rateNumber, inputData);
        RateAmounts rateAmounts = amountsCalculationServie.calculate();
        MortgageResidual mortgageResidual = residualCalculationService.calculate();

        return new Rate(rateNumber, timePoint, rateAmounts, mortgageResidual);
    }

    private Rate calculateRate(BigDecimal rateNumber, InputData inputData, Rate previousRate) {
        TimePoint timePoint = timePointService.calculate(rateNumber, inputData);
        RateAmounts rateAmounts = amountsCalculationServie.calculate();
        MortgageResidual mortgageResidual = residualCalculationService.calculate();

        return new Rate(rateNumber, timePoint, rateAmounts, mortgageResidual);
    }


}
