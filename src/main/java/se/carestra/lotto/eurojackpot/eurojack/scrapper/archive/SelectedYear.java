package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;

record SelectedYear(SelectedYearButton button, Document document) {
  SelectedYear {
    if (button == null) {
      throw new IllegalArgumentException("button cannot be null.");
    }

    if (document == null) {
      throw new IllegalArgumentException("document cannot be null.");
    }
  }
}
