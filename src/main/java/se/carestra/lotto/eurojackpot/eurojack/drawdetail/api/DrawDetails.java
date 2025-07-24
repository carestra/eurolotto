package se.carestra.lotto.eurojackpot.eurojack.drawdetail.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public record DrawDetails(LocalDate drawDate,
                          SelectedBallNumbers selectedBallNumbers,
                          EuroBallNumbers euroBallNumbers,
                          JackpotDetail jackpotDetail,
                          String resourceUri,
                          String fullPath) {
  public DrawDetails {
    if (selectedBallNumbers == null) {
      throw new NullPointerException("selectedBallNumbers cannot be null.");
    }
    if (euroBallNumbers == null) {
      throw new NullPointerException("euroBallNumbers cannot be null.");
    }
    if (jackpotDetail == null) {
      throw new NullPointerException("jackpotDetail cannot be null.");
    }
    if (resourceUri == null) {
      throw new NullPointerException("resourceUri cannot be null.");
    }
    if (fullPath == null) {
      throw new NullPointerException("fullPath cannot be null.");
    }
  }

  public DrawDetails(SelectedBallNumbers selectedBallNumbers,
                     EuroBallNumbers euroBallNumbers,
                     JackpotDetail jackpotDetail,
                     String resourceUri,
                     String fullPath) {
    this(extractAndParseDatePart(resourceUri), selectedBallNumbers, euroBallNumbers, jackpotDetail, resourceUri, fullPath);
  }

  private static final String DETAIL_URI_PATH_PREFIX = "/results/";
  private static final DateTimeFormatter DATE_TEXT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

  private static LocalDate extractAndParseDatePart(String resourceUri) {
    return LocalDate.from(parseUriDatePart(resourceUri));
  }

  private static TemporalAccessor parseUriDatePart(String path) {
    try {
      return DATE_TEXT_FORMATTER.parse(path.replaceAll(DETAIL_URI_PATH_PREFIX, ""));
    } catch (DateTimeParseException parseException) {
      throw new IllegalArgumentException("Invalid resourceUri format [" + path + "]. Expected '/results/<dd-MM-YYYY>'.");
    }
  }
}
