package service;

import model.InputData;
import model.Rate;
import model.Summary;

import java.util.List;

public class MortgageCalculationServiceImpl implements MortgageCalculationService {

    private final RateCalculationService rateCalculationService;

    private final PrintingService printingService;

    private final SummaryService summaryService;

    public MortgageCalculationServiceImpl(
            final RateCalculationService rateCalculationService,
            final PrintingService printingService,
            final SummaryService summaryService
    ) {
        this.rateCalculationService = rateCalculationService;
        this.printingService = printingService;
        this.summaryService = summaryService;
    }

    @Override
    public void calculate(InputData inputData) {
        printingService.printIntroInformation(inputData);

        List<Rate> rates = rateCalculationService.calculate(inputData);
        Summary summary = summaryService.calculateSummary(rates);

        printingService.printSummary(summary);
        printingService.printSchedule(rates, inputData);
    }

}
