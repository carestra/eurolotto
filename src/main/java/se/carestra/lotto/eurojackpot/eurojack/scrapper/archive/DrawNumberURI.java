package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

public record DrawNumberURI(String path, String archiveUrl) {
  public DrawNumberURI {
    if (path == null || archiveUrl == null) {
      throw new IllegalArgumentException("path or archiveUrl cannot be null.");
    }
  }
}
