package se.carestra.lotto.eurojackpot.eurojack.archive.api;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface DrawNumberURIScrapper {

  Optional<List<DrawNumberURI>> fetch(@NonNull String archiveYear);

}
