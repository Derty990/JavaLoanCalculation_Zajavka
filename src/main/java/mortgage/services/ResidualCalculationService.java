package mortgage.services;

import mortgage.model.InputData;
import mortgage.model.MortgageResidual;
import mortgage.model.Rate;
import mortgage.model.RateAmounts;

public interface ResidualCalculationService {

    MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData);

    MortgageResidual calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate);

}
