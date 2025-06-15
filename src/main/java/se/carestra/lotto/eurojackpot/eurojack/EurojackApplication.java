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

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(EurojackApplication.class, args);

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
