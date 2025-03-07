package model;

import java.math.BigDecimal;

public class Summary {

    private final BigDecimal interestSum;
    private final BigDecimal overpaymentProvisionSum;
    private final BigDecimal totalLostSum;
    private final BigDecimal totalCapital;

    public Summary(final BigDecimal interestSum, final BigDecimal overpaymentProvisionSum, final BigDecimal totalLostSum, final BigDecimal totalCapital) {
        this.interestSum = interestSum;
        this.overpaymentProvisionSum = overpaymentProvisionSum;
        this.totalLostSum = totalLostSum;
        this.totalCapital = totalCapital;
    }

    public BigDecimal getInterestSum() {
        return interestSum;
    }

    public BigDecimal getOverpaymentProvisionSum() {
        return overpaymentProvisionSum;
    }

    public BigDecimal getTotalLostSum() {
        return totalLostSum;
    }

    public BigDecimal getTotalCapital() {
        return totalCapital;
    }
}
