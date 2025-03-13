package mortgage.services;

import mortgage.model.InputData;
import mortgage.model.MortgageType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputDataService {
    private static final Path FILE_LOCATION = Paths.get("F:\\Zajavka\\Warsztaty\\MortgageCalculatorProject\\Project\\src\\main\\resources\\inputData.csv");

    public InputData read() throws IOException {
        var content = Files.readString(FILE_LOCATION)
                .lines()
                .collect(Collectors.groupingBy(line -> line.split(";")[0]));

        validate(content);

        var inputData = content.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0).split(";")[1]));

        return new InputData()
                .withRepaymentStartDate(
                        Optional.ofNullable(inputData.get("repaymentStartDate")).map(LocalDate::parse).orElseThrow())
                .withWiborPercent(
                        Optional.ofNullable(inputData.get("wiborPercent")).map(BigDecimal::new).orElseThrow())
                .withAmount(
                        Optional.ofNullable(inputData.get("amount")).map(BigDecimal::new).orElseThrow())
                .withMonthsDuration(
                        Optional.ofNullable(inputData.get("monthsDuration")).map(BigDecimal::new).orElseThrow())
                .withRateType(
                        Optional.ofNullable(inputData.get("rateType")).map(MortgageType::valueOf).orElseThrow())
                .withMarginPercent(
                        Optional.ofNullable(inputData.get("marginPercent")).map(BigDecimal::new).orElseThrow())
                .withOverpaymentProvisionPercent(
                        Optional.ofNullable(inputData.get("overpaymentProvisionPercent")).map(BigDecimal::new).orElseThrow())
                .withOverpaymentProvisionMonths(
                        Optional.ofNullable(inputData.get("overpaymentProvisionMonths")).map(BigDecimal::new).orElseThrow())
                .withOverpaymentStartMonth(
                        Optional.ofNullable(inputData.get("overpaymentStartMonth")).map(BigDecimal::new).orElseThrow())
                .withOverpaymentSchema(
                        Optional.ofNullable(inputData.get("overpaymentSchema")).map(this::calculateSchema).orElseThrow())
                .withOverpaymentReduceWay(
                        Optional.ofNullable(inputData.get("overpaymentReduceWay")).orElseThrow())
                .withMortgagePrintPayoffsSchedule(
                        Optional.ofNullable(inputData.get("mortgagePrintPayoffsSchedule")).map(Boolean::parseBoolean).orElseThrow())
                .withMortgageRateNumberToPrint(
                        Optional.ofNullable(inputData.get("mortgageRateNumberToPrint")).map(Integer::parseInt).orElseThrow());

    }

    private Map<Integer, BigDecimal> calculateSchema(String schema) {
        return Stream.of(schema.split(","))
                .map(entry -> Map.entry(entry.split(":")[0], entry.split(":")[1]))
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()),
                        entry -> new BigDecimal(entry.getValue()),
                        (v1, v2) -> v2,
                        TreeMap::new
                ));
    }

    private void validate(final Map<String, List<String>> content) {
        if (content.values().stream().anyMatch(values -> values.size() != 1)) {
            throw new IllegalArgumentException("Configuration mismatch");
        }
    }


}
