package mortgage.services;

import mortgage.model.InputData;
import mortgage.model.Rate;

import java.util.List;

public interface RateCalculationService {

    List<Rate> calculate(InputData inputData);
}
