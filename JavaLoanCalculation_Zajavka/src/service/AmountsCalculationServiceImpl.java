package service;

import model.InputData;
import model.Overpayment;
import model.Rate;
import model.RateAmounts;


public class AmountsCalculationServiceImpl implements AmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;

    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;

    public AmountsCalculationServiceImpl(
            ConstantAmountsCalculationService constantAmountsCalculationService,
            DecreasingAmountsCalculationService decreasingAmountsCalculationService
    ) {
        this.constantAmountsCalculationService = constantAmountsCalculationService;
        this.decreasingAmountsCalculationService = decreasingAmountsCalculationService;
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        switch (inputData.getRateType()) {
            case CONSTANT -> {
                return constantAmountsCalculationService.calculate(inputData, overpayment);
            }

            case DECREASING -> {
                return decreasingAmountsCalculationService.calculate(inputData, overpayment);
            }

            default -> throw new MortgageException();
        }
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        switch (inputData.getRateType()) {
            case CONSTANT -> {
                return constantAmountsCalculationService.calculate(inputData, overpayment, previousRate);
            }

            case DECREASING -> {
                return decreasingAmountsCalculationService.calculate(inputData, overpayment, previousRate);
            }

            default -> throw new MortgageException();
        }
    }
}







