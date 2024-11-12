package service;

import model.InputData;
import model.MortgageResidual;
import model.Rate;
import model.RateAmounts;

import java.math.BigDecimal;

public class ResidualCalculationServiceImpl implements ResidualCalculationService {


    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData) {

        BigDecimal residualAmount = inputData.getAmount().subtract(rateAmounts.getCapitalAmount());
        BigDecimal residualDuration = inputData.getMonthsDuration().subtract(BigDecimal.ONE);


        return new MortgageResidual(residualAmount, residualDuration);

    }

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, Rate previousRate) {
        MortgageResidual residual = previousRate.getMortgageResidual();

        BigDecimal residualAmount = residual.getAmount().subtract(rateAmounts.getCapitalAmount());
        BigDecimal residualDuration = residual.getDuration().subtract(BigDecimal.ONE);


        return new MortgageResidual(residualAmount, residualDuration);

    }

}
