package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import java.util.Optional;

public record DrawNumberURI(String path, String archiveUrl) {
  public DrawNumberURI {
    if (path == null || archiveUrl == null) {
      throw new IllegalArgumentException("path or archiveUrl cannot be null.");
    }
  }

  public static Optional<DrawNumberURI> parse(String uri, String archiveUrl) {
    if (uri == null || archiveUrl == null) {
      return Optional.empty();
    } else {
      return Optional.of(new DrawNumberURI(uri, archiveUrl));
    }
  }
}
