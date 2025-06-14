package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public record DrawNumberURI(String path, String archiveUrl) {
  public DrawNumberURI {
    if (path == null || archiveUrl == null) {
      throw new IllegalArgumentException("path or archiveUrl cannot be null.");
    }
    // validate path contains date
    parsePath(path);
  }

  private static final String PATH_PREFIX = "/results/";
  private static final DateTimeFormatter DATE_TEXT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

  public LocalDate drawDate() {
    return LocalDate.from(parsePath(path));
  }

  private TemporalAccessor parsePath(String path) {
    try {
      return DATE_TEXT_FORMATTER.parse(path.replaceAll(PATH_PREFIX, ""));
    } catch (DateTimeParseException parseException) {
      throw new IllegalArgumentException("Invalid path format [" + path + "]. Expected '/results/<dd-MM-YYYY>'.");
    }
  }
}
