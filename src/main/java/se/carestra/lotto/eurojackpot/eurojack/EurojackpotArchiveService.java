package se.carestra.lotto.eurojackpot.eurojack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.DrawNumberURI;
import se.carestra.lotto.eurojackpot.eurojack.archive.DrawNumberURIScrapper;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.DrawDetailScrapper;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.DrawDetails;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.DrawResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EurojackpotArchiveService {
  private final DrawNumberURIScrapper resourceScrapper;
  private final DrawDetailScrapper drawDetailScrapper;


  public EurojackpotArchiveService(@Autowired DrawNumberURIScrapper resourceScrapper,
                                   @Autowired DrawDetailScrapper drawDetailScrapper) {
    this.resourceScrapper = resourceScrapper;
    this.drawDetailScrapper = drawDetailScrapper;
  }

  public Optional<List<DrawResult>> fetchArchiveYear(String archiveYear) {
    Optional<List<DrawNumberURI>> resourcesOptional = resourceScrapper.fetch(archiveYear);

    Optional<List<DrawDetails>> drawDetails =
        resourcesOptional
            .map(resources ->
                resources.stream()
                    .map(resource ->
                        drawDetailScrapper.fetchDetails(resource.resourceUri())
                    )
                    .flatMap(Optional::stream)
                    .toList()
            );

    return drawDetails
        .map(details ->
            details.stream()
                .map(DrawResult::convert)
                .collect(Collectors.toList())
        );
  }

}
