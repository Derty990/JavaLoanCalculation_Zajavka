package mortgage.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mortgage.model.InputData;
import mortgage.model.Overpayment;
import mortgage.model.Rate;
import mortgage.model.Summary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PrintingServiceImpl implements PrintingService {

    @Override
    public void printIntroInformation(InputData inputData) {
        StringBuilder msg = new StringBuilder(NEW_LINE);
        msg.append(MORTGAGE_AMOUNT).append(inputData.getAmount()).append(CURRENCY);
        msg.append(NEW_LINE);
        msg.append(MORTGAGE_PERIOD).append(inputData.getMonthsDuration()).append(MONTHS);
        msg.append(NEW_LINE);
        msg.append(INTEREST).append(inputData.getInterestToDisplay()).append(PERCENT);
        msg.append(NEW_LINE);
        msg.append(OVERPAYMENT_START_MONTH).append(inputData.getOverpaymentStartMonth()).append(BLANK).append(MONTH);

        Optional.of(inputData.getOverpaymentSchema())
                .filter(schema -> !schema.isEmpty())
                .ifPresent(schema -> logOverpayment(msg, inputData.getOverpaymentSchema(), inputData.getOverpaymentReduceWay()));

        log(msg.toString());
    }

    private void logOverpayment(final StringBuilder msg, final Map<Integer, BigDecimal> schema, final String reduceWay) {
        switch (reduceWay) {
            case Overpayment.REDUCE_PERIOD:
                msg.append(OVERPAYMENT_REDUCE_PERIOD);
                break;
            case Overpayment.REDUCE_RATE:
                msg.append(OVERPAYMENT_REDUCE_RATE);
                break;
            default:
                throw new MortgageException("Case not handled");
        }
        msg.append(NEW_LINE);
        msg.append(OVERPAYMENT_FREQUENCY).append(NEW_LINE).append(prettyPrintOverpaymentSchema(schema));
    }

    private String prettyPrintOverpaymentSchema(Map<Integer, BigDecimal> schema) {
        StringBuilder schemaMsg = new StringBuilder();
        for (Map.Entry<Integer, BigDecimal> entry : schema.entrySet()) {
            schemaMsg.append(MONTH)
                    .append(entry.getKey())
                    .append(COMMA)
                    .append(AMOUNT)
                    .append(entry.getValue())
                    .append(CURRENCY)
                    .append(NEW_LINE);
        }
        return schemaMsg.toString();
    }

    @Override
    public void printSchedule(final List<Rate> rates, final InputData inputData) {
        if (!inputData.isMortgagePrintPayoffsSchedule()) {
            return;
        }

        int index = 1;
        for (Rate rate : rates) {
            if (rate.getRateNumber().remainder(BigDecimal.valueOf(inputData.getMortgageRateNumberToPrint())).equals(BigDecimal.ZERO)) {
                String message = String.format(SCHEDULE_TABLE_FORMAT,
                        RATE_NUMBER, rate.getRateNumber(),
                        YEAR, rate.getTimePoint().getYear(),
                        MONTH, rate.getTimePoint().getMonth(),
                        DATE, rate.getTimePoint().getDate(),
                        RATE, rate.getRateAmounts().getRateAmount(),
                        INTEREST, rate.getRateAmounts().getInterestAmount(),
                        CAPITAL, rate.getRateAmounts().getCapitalAmount(),
                        OVERPAYMENT, rate.getRateAmounts().getOverpayment().getAmount(),
                        LEFT_AMOUNT, rate.getMortgageResidual().getResidualAmount(),
                        LEFT_MONTHS, rate.getMortgageResidual().getResidualDuration()
                );
                log(message);
                if (index % AmountsCalculationService.YEAR.intValue() == 0) {
                    log(SEPARATOR);
                }
                index++;
            }
        }
        log(NEW_LINE);
    }

    @Override
    public void printSummary(final Summary summary) {
        String msg = INTEREST_SUM + summary.getInterestSum() + CURRENCY +
                NEW_LINE +
                OVERPAYMENT_PROVISION + summary.getOverpaymentProvisionSum().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
                NEW_LINE +
                LOSTS_SUM + summary.getTotalLostSum().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
                NEW_LINE +
                CAPITAL_SUM + summary.getTotalCapital().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
                NEW_LINE;

        log(msg);
    }

    @SuppressWarnings("SameParameterValue")
    private void log(String message) {
        //log.info(message);
        System.out.println(message);
    }

}
