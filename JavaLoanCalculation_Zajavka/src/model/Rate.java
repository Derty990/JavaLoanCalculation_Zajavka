package model;

import java.math.BigDecimal;

public class Rate {

    private final BigDecimal rateNumber;

    private final TimePoint timePoint;

    private final RateAmounts rateAmounts;

    private final MortgageResidual mortgageResidual;

    private final MortgageReference mortgageReference;

    public Rate(
            final BigDecimal rateNumber,
            final TimePoint timePoint,
            final RateAmounts rateAmounts,
            final MortgageResidual mortgageResidual,
            final MortgageReference mortgageReference
    ) {
        this.rateNumber = rateNumber;
        this.timePoint = timePoint;
        this.rateAmounts = rateAmounts;
        this.mortgageResidual = mortgageResidual;
        this.mortgageReference = mortgageReference;
    }

    public BigDecimal getRateNumber() {
        return rateNumber;
    }

    public TimePoint getTimePoint() {
        return timePoint;
    }

    public RateAmounts getRateAmounts() {
        return rateAmounts;
    }

    public MortgageResidual getMortgageResidual() {
        return mortgageResidual;
    }

    public MortgageReference getMortgageReference() {
        return mortgageReference;
    }
}
