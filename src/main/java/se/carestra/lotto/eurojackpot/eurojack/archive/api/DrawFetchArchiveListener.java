package se.carestra.lotto.eurojackpot.eurojack.archive.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.repository.DrawNumberURIRepository;

@Service
public class DrawFetchArchiveListener {
  private final DrawNumberURIScrapper scrapper;
  private final DrawNumberURIRepository repository;
  private static final Logger LOGGER = LoggerFactory.getLogger(DrawFetchArchiveListener.class);

  public DrawFetchArchiveListener(@Autowired DrawNumberURIScrapper scrapper,
                                  @Autowired DrawNumberURIRepository repository) {
    this.scrapper = scrapper;
    this.repository = repository;
  }

  /**
   * Method is synchronized since we are querying and saving to a shared resource (db).
   * Otherwise multiple threads will cause race condition when both act on samme event, both read db
   * and got response that the archive year is not found, but ony one will save fetched values, the other
   * will throw duplication key exception.
   *
   * @param event
   */
  @ApplicationModuleListener
  synchronized void on(FetchArchiveEvent event) {
    boolean hasArchiveYear = repository.hasArchiveYear(event.yearAsInt());
    if (hasArchiveYear) {
      LOGGER.info("Archive year {} already fetched.", event.year());
    } else {
      scrapper.fetch(event.year())
          .ifPresentOrElse(
              archive -> {
                LOGGER.info("Archive {} fetched and contained {} draws.", event.year(), archive.size());
                repository.saveAll(archive);
              },
              () -> LOGGER.info("No archive found for year {}", event.year())
          );
    }
  }
}
