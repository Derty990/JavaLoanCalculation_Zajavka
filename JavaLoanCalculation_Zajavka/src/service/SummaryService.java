package service;

import model.Rate;
import model.Summary;

import java.util.List;

public interface SummaryService {

    Summary calculate(List<Rate> rates) ;

}
