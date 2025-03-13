package mortgage;

import mortgage.model.InputData;
import mortgage.model.MortgageType;
import mortgage.model.Overpayment;
import mortgage.services.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MortgageCalculator {

    public static void main(String[] args) {

        InputData inputData;

        try {
           inputData = new InputDataService().read();
        } catch (Exception e) {
            System.err.println("Error loading input data, interrupting. Error: "+e.getMessage());
            e.printStackTrace();
        }

        System.out.println("TEST");

       /* Map<Integer, BigDecimal> overpaymentSchema = new TreeMap<>();
        overpaymentSchema.put(5, BigDecimal.valueOf(12000));
        overpaymentSchema.put(19, BigDecimal.valueOf(10000));
        overpaymentSchema.put(28, BigDecimal.valueOf(11000));
        overpaymentSchema.put(64, BigDecimal.valueOf(16000));
        overpaymentSchema.put(78, BigDecimal.valueOf(18000));

        InputData defaultInputData = new InputData()
                .withAmount(new BigDecimal("296192.11"))
                .withMonthsDuration(BigDecimal.valueOf(360))
                .withOverpaymentReduceWay(Overpayment.REDUCE_RATE)
                .withRateType(MortgageType.CONSTANT)
                .withOverpaymentSchema(overpaymentSchema);

        CalculatorCreator.getInstance().calculate(defaultInputData);*/

    }

  /*  static class CalculatorCreator {
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


    }
*/

}
