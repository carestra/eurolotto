package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import java.time.LocalDate;
import java.util.List;

public record DrawDetail(LocalDate date,
                         List<Integer> ballNumbers,
                         List<Integer> euroBallNumbers,
                         String jackpotAmountFormatted,
                         String currencySymbol,
                         Integer rollover,
                         Integer nrOfWinners,
                         String resourceUri,
                         String fullPath) {
}
