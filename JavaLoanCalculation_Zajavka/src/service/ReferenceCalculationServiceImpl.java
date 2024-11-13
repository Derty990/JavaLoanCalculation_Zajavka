package service;

import model.InputData;
import model.MortgageReference;

public class ReferenceCalculationServiceImpl implements ReferenceCalculationService {
    @Override
    public MortgageReference calculate(InputData inputData) {
        return new MortgageReference(inputData.getAmount(), inputData.getMonthsDuration());
    }
}
