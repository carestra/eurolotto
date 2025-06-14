package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;
import org.springframework.lang.NonNull;

import java.util.Optional;

record ResultDrawTable(DrawTable exist, @NonNull Optional<Element> optionalTableElement) {
  ResultDrawTable {
    if (exist == null) {
      throw new IllegalArgumentException("Draw table not found.");
    }
  }
}
