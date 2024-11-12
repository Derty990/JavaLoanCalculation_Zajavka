package service;

import model.Rate;

import java.math.BigDecimal;

public interface Function {

    BigDecimal calculate(Rate rate);

}
