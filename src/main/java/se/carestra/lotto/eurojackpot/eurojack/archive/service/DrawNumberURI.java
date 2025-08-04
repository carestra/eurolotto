package se.carestra.lotto.eurojackpot.eurojack.archive.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public record DrawNumberURI(String resourceUri, String archiveUrl) {
  public DrawNumberURI {
    if (resourceUri == null || archiveUrl == null) {
      throw new IllegalArgumentException("resourceUri and archiveUrl cannot be null.");
    }
    // validate resourceUri contains the draw date
    parseUriDatePart(resourceUri);
  }

  private static final String DETAIL_URI_PATH_PREFIX = "/results/";
  public static final DateTimeFormatter DATE_TEXT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

  public LocalDate drawDate() {
    return LocalDate.from(parseUriDatePart(resourceUri));
  }

  private TemporalAccessor parseUriDatePart(String path) {
    try {
      return DATE_TEXT_FORMATTER.parse(path.replaceAll(DETAIL_URI_PATH_PREFIX, ""));
    } catch (DateTimeParseException parseException) {
      throw new IllegalArgumentException("Invalid resourceUri format [" + path + "]. Expected '/results/<dd-MM-YYYY>'.");
    }
  }
}
