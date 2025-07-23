package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import java.time.LocalDate;

public record DrawResourceURI(LocalDate drawDate, String resourceUri, String archiveUrl) {
}
