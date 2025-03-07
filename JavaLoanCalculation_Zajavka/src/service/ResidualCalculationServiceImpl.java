package service;

import model.InputData;
import model.MortgageResidual;
import model.Rate;
import model.RateAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ResidualCalculationServiceImpl implements ResidualCalculationService {

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.getAmount())) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(inputData.getAmount(), rateAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, inputData.getMonthsDuration(), rateAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate) {
        BigDecimal previousResidualAmount = previousRate.getMortgageResidual().getResidualAmount();
        BigDecimal previousResidualDuration = previousRate.getMortgageResidual().getResidualDuration();

        if (BigDecimal.ZERO.equals(previousResidualAmount)) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(previousResidualAmount, rateAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, previousResidualDuration, rateAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    private BigDecimal calculateResidualDuration(
            InputData inputData,
            BigDecimal residualAmount,
            BigDecimal previousResidualDuration,
            RateAmounts rateAmounts
    ) {
        // trzeba przeliczyć kredyt w zależności od tego czy podczas nadpłaty zmniejszamy czas trwania czy wysokość raty
        if (rateAmounts.getOverpayment().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            switch (inputData.getRateType()) {
                case CONSTANT:
                    return calculateConstantResidualDuration(inputData, residualAmount, rateAmounts);
                case DECREASING:
                    return calculateDecreasingResidualDuration(residualAmount, rateAmounts);
                default:
                    throw new MortgageException("Case not handled");
            }
        } else {
            // w każdym normalnym przypadku z miesiąca na miesiąc ilość pozostałych miesięcy jest zmniejszna o 1
            return previousResidualDuration.subtract(BigDecimal.ONE);
        }
    }

    private BigDecimal calculateDecreasingResidualDuration(BigDecimal residualAmount, RateAmounts rateAmounts) {
        return residualAmount.divide(rateAmounts.getCapitalAmount(), 0, RoundingMode.CEILING);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    //jak oszacować ilość miesięcy jaka nam pozostała do spłaty przy nadpłacie, ratach stałych i zmniejszeniu czasu trwania.
    private BigDecimal calculateConstantResidualDuration(InputData inputData, BigDecimal residualAmount, RateAmounts rateAmounts) {
        // log_y(x) = log(x) / log (y)
        BigDecimal q = AmountsCalculationService.calculateQ(inputData.getInterestPercent());

        // licznik z logarytmu z licznika wzoru końcowego
        BigDecimal xNumerator = rateAmounts.getRateAmount();
        // mianownik z logarytmu z licznika wzoru końcowego. b/m to równie dobrze q-1
        BigDecimal xDenominator = rateAmounts.getRateAmount().subtract(residualAmount.multiply(q.subtract(BigDecimal.ONE)));

        BigDecimal x = xNumerator.divide(xDenominator, 10, RoundingMode.HALF_UP);
        BigDecimal y = q;

        // logarytm z licznika
        BigDecimal logX = BigDecimal.valueOf(Math.log(x.doubleValue()));
        // logarytm z mianownika
        BigDecimal logY = BigDecimal.valueOf(Math.log(y.doubleValue()));

        return logX.divide(logY, 0, RoundingMode.CEILING);
    }

    private BigDecimal calculateResidualAmount(final BigDecimal residualAmount, final RateAmounts rateAmounts) {
        return residualAmount
                .subtract(rateAmounts.getCapitalAmount())
                .subtract(rateAmounts.getOverpayment().getAmount())
                .max(BigDecimal.ZERO);
    }

}

