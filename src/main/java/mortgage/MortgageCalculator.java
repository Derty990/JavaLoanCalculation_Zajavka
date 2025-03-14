package mortgage;

import mortgage.configuration.CalculatorConfiguration;
import mortgage.model.InputData;
import mortgage.model.MortgageType;
import mortgage.model.Overpayment;
import mortgage.services.InputDataRepository;
import mortgage.services.MortgageCalculationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class MortgageCalculator {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(CalculatorConfiguration.class);

        InputDataRepository inputDataRepository = context.getBean(InputDataRepository.class);


        final var inputData = inputDataRepository.read();
        if (inputData.isEmpty()) {
            return;
        }

        System.out.println("TEST");

        Map<Integer, BigDecimal> overpaymentSchema = new TreeMap<>(
                Map.of(
                        5, BigDecimal.valueOf(12000),
                        19, BigDecimal.valueOf(10000),
                        28, BigDecimal.valueOf(11000),
                        64, BigDecimal.valueOf(16000),
                        78, BigDecimal.valueOf(18000)
                )
        );

        InputData updatedInputData = inputData.get()
                .withAmount(new BigDecimal("296192.11"))
                .withMonthsDuration(BigDecimal.valueOf(360))
                .withOverpaymentReduceWay(Overpayment.REDUCE_RATE)
                .withRateType(MortgageType.CONSTANT)
                .withOverpaymentSchema(overpaymentSchema);

        MortgageCalculationService mortgageCalculationService = context.getBean(MortgageCalculationService.class);

        mortgageCalculationService.calculate(updatedInputData);

    }

    /// This is simplified by using spring beans and context
    /*
    static class CalculatorCreator {
        private static MortgageCalculationService instance;

        private CalculatorCreator() {

        }

        public static MortgageCalculationService getInstance() {
            if (Objects.isNull(instance)) {
                instance = new MortgageCalculationServiceImpl(
                        new RateCalculationServiceImpl(
                                new TimePointCalculationServiceImpl(),
                                new OverpaymentCalculationServiceImpl(),
                                new AmountsCalculationServiceImpl(
                                        new ConstantAmountsCalculationServiceImpl(),
                                        new DecreasingAmountsCalculationServiceImpl()
                                ),
                                new ResidualCalculationServiceImpl(),
                                new ReferenceCalculationServiceImpl()
                        ),
                        new PrintingServiceImpl(),
                        SummaryServiceFactory.create()
                );
            }
            return instance;
        }


    }*/

}
