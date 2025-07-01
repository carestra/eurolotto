package se.carestra.lotto.eurojackpot.eurojack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURIScrapper;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.DrawDetailScrapper;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.DrawDetails;

import java.util.List;
import java.util.Optional;

@Service
public class EurojackpotArchiveService {
  private final DrawNumberURIScrapper resourceScrapper;
  private final DrawDetailScrapper drawDetailScrapper;


  public EurojackpotArchiveService(@Autowired DrawNumberURIScrapper resourceScrapper,
                                   @Autowired DrawDetailScrapper drawDetailScrapper) {
    this.resourceScrapper = resourceScrapper;
    this.drawDetailScrapper = drawDetailScrapper;
  }

  public Optional<List<DrawDetails>> fetchArchiveYear(String archiveYear) {
    Optional<List<DrawNumberURI>> resourcesOptional = resourceScrapper.fetch(archiveYear);

    return resourcesOptional
        .map(resources ->
            resources.stream()
                .map(resource ->
                    drawDetailScrapper.fetchDetails(resource.resourceUri())
                )
                .flatMap(Optional::stream)
                .toList()
        );
  }

}
