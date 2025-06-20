package se.carestra.lotto.eurojackpot.eurojack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.ScrapperHeaders;

@SpringBootApplication
@EnableConfigurationProperties(ScrapperHeaders.class)
@EnableJpaAuditing
public class EurojackApplication {

  /**
   * Archive
   * https://www.euro-jackpot.net/results-archive-2012
   * <p>
   * Detail
   * https://www.euro-jackpot.net/results/01-06-2012
   *
   * @param args
   */
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(EurojackApplication.class, args);

    // TODO: improve; will be block and service would not be able to do web crawl.
//    EurojackpotArchiveService service = context.getBean(EurojackpotArchiveService.class);
//    Optional<List<DrawResult>> optionalResults = service.fetchArchiveYear("2012");
//    optionalResults.ifPresent(results -> results.forEach(System.out::println));

//    DrawDetailScrapper detailScrapper = context.getBean(DrawDetailScrapper.class);
//    Optional<DrawDetails> optionalResults = detailScrapper.fetchDetails("/results/01-06-2012");
//    optionalResults.ifPresent(System.out::println);

//    DrawNumberURIRepository resourceRepo = context.getBean(DrawNumberURIRepository.class);
//    DrawNumberURI resource = new DrawNumberURI("/results/15-06-2020", "htt://lotto.mock.dot");
//    DrawNumberURI savedResource = resourceRepo.save(resource);
//    System.out.printf("Saved resource URI: %s%n", savedResource);
//
//    DrawDetailsRepository repository = context.getBean(DrawDetailsRepository.class);
//    DrawResult drawResult = DrawResult.builder()
//        .drawDate(LocalDate.now())
//        .ballsDRawOrder(List.of(40, 5, 27, 9, 18))
//        .euroBallsDRawOrder(List.of(2, 11))
//        .jackpotAmount(new BigInteger("300000"))
//        .currencySymbol("kr")
//        .rollover(0)
//        .jackpotWinnersCount(0)
//        .archiveUrl("htt://lotto.mock.dot/results/15-06-2020")
//        .build();
//    DrawResult savedDrawResult = repository.save(drawResult);
//    System.out.printf("Saved drawResult: %s%n", savedDrawResult);
  }

}
