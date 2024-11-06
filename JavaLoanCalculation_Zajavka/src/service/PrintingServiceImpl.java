package service;

import model.InputData;

public class PrintingServiceImpl implements PrintingService {

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public void printInputDataInfo(InputData inputData) {
        StringBuilder msg = new StringBuilder(NEW_LINE);
        msg.append(MORTGAGE_AMOUNT).append(inputData.getAmount()).append(CURRENCY);
        msg.append(NEW_LINE);
        msg.append(MORTGAGE_AMOUNT).append(inputData.getMonthsDuration()).append(MONTHS);
        msg.append(NEW_LINE);
        msg.append(INTEREST).append(inputData.getInterestDisplay()).append(PERCENT);
        msg.append(NEW_LINE);

        printMessage(msg);

    }

    private void printMessage(StringBuilder sb){
        System.out.println(sb);
    }


}
