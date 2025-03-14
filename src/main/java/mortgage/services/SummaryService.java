package mortgage.services;

import mortgage.model.Rate;
import mortgage.model.Summary;

import java.util.List;

@FunctionalInterface
public interface SummaryService {

    Summary calculateSummary(List<Rate> rates);
}
