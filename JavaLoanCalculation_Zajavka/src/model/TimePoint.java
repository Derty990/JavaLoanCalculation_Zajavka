package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TimePoint {

    private final BigDecimal year;

    private final BigDecimal month;

    private final LocalDate date;

    public TimePoint(final BigDecimal year, final BigDecimal month, final LocalDate date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public BigDecimal getYear() {
        return year;
    }

    public BigDecimal getMonth() {
        return month;
    }

    public LocalDate getDate() {
        return date;
    }
}
