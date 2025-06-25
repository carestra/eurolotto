package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public record DrawDetails(EurojackpotDraw draw,
                          JackpotDetail jackpotDetail,
                          String resourceUri,
                          String fullPath) {
    public DrawDetails {
        if (draw == null) {
            throw new NullPointerException("draw cannot be null.");
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

    private static final String DETAIL_URI_PATH_PREFIX = "/results/";
    private static final DateTimeFormatter DATE_TEXT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

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
