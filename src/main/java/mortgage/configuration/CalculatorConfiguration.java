package mortgage.configuration;

import mortgage.model.Rate;
import mortgage.model.RateAmounts;
import mortgage.model.Summary;
import mortgage.services.SummaryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Configuration
@ComponentScan(basePackages = "mortgage")
public class CalculatorConfiguration {

    @Bean
    public static SummaryService create() {
        return rates -> {
            BigDecimal interestSum = calculate(rates, rate -> rate.getRateAmounts().getInterestAmount());
            BigDecimal overpaymentProvisionSum = calculate(rates, rate -> rate.getRateAmounts().getOverpayment().getProvisionAmount());
            BigDecimal totalLostSum = interestSum.add(overpaymentProvisionSum);
            BigDecimal totalCapital = calculate(rates, rate -> totalCapital(rate.getRateAmounts()));
            return new Summary(interestSum, overpaymentProvisionSum, totalLostSum, totalCapital);
        };
    }

    private static BigDecimal totalCapital(final RateAmounts rateAmounts) {
        return rateAmounts.getCapitalAmount().add(rateAmounts.getOverpayment().getAmount());
    }

    private static BigDecimal calculate(final List<Rate> rates, Function<Rate, BigDecimal> function) {
        return rates.stream()
                .reduce(BigDecimal.ZERO, (sum, next) -> sum.add(function.apply(next)), BigDecimal::add);

    }
}
