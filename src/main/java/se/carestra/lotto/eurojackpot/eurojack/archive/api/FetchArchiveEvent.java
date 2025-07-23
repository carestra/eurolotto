package se.carestra.lotto.eurojackpot.eurojack.archive.api;

public record FetchArchiveEvent(String year) {
  public int yearAsInt() {
    return Integer.parseInt(year);
  }
}
