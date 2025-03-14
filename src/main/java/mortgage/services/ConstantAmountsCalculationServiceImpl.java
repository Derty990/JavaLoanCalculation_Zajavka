package mortgage.services;

import lombok.extern.slf4j.Slf4j;
import mortgage.model.InputData;
import mortgage.model.Overpayment;
import mortgage.model.Rate;
import mortgage.model.RateAmounts;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class ConstantAmountsCalculationServiceImpl implements ConstantAmountsCalculationService {

    @Override
    public RateAmounts calculate(final InputData inputData, final Overpayment overpayment) {

        BigDecimal interestPercent = inputData.getInterestPercent();
        log.debug("interestPercent [{}]", interestPercent);
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);
        log.trace("q: [{}]", q);
        BigDecimal residualAmount = inputData.getAmount();
        log.info("residualAmount: [{}]", residualAmount);
        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.info("interestAmount: [{}]", interestAmount);
        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, inputData.getAmount(), inputData.getMonthsDuration());
        log.info("rateAmount: [{}]", rateAmount);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);
        log.info("capitalAmount: [{}]", capitalAmount);
        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(final InputData inputData, final Overpayment overpayment, final Rate previousRate) {

        BigDecimal interestPercent = inputData.getInterestPercent();
        log.debug("interestPercent [{}]", interestPercent);

        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);
        log.trace("q: [{}]", q);

        BigDecimal residualAmount = previousRate.getMortgageResidual().getResidualAmount();
        log.info("residualAmount: [{}]", residualAmount);
        BigDecimal referenceAmount = previousRate.getMortgageReference().getReferenceAmount();
        log.info("referenceAmount: [{}]", referenceAmount);
        BigDecimal referenceDuration = previousRate.getMortgageReference().getReferenceDuration();
        log.info("referenceDuration: [{}]", referenceDuration);

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.info("interestAmount: [{}]", interestAmount);
        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, referenceAmount, referenceDuration);
        log.info("rateAmount: [{}]", rateAmount);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);
        log.info("capitalAmount: [{}]", capitalAmount);
        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    private BigDecimal calculateConstantRateAmount(
            final BigDecimal q,
            final BigDecimal interestAmount,
            final BigDecimal residualAmount,
            final BigDecimal referenceAmount,
            final BigDecimal referenceDuration
    ) {
        BigDecimal rateAmount = referenceAmount
                .multiply(q.pow(referenceDuration.intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide(q.pow(referenceDuration.intValue()).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        log.info("RateAmount [{}]", rateAmount);
        return compareRateWithResidual(rateAmount, interestAmount, residualAmount);
    }

    private BigDecimal compareRateWithResidual(final BigDecimal rateAmount, final BigDecimal interestAmount, final BigDecimal residualAmount) {
        if (rateAmount.subtract(interestAmount).compareTo(residualAmount) >= 0) {
            return residualAmount.add(interestAmount);
        }
        return rateAmount;
    }

}
