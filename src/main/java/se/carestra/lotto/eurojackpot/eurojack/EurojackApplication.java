package se.carestra.lotto.eurojackpot.eurojack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.ScrapperHeaders;

@SpringBootApplication
@EnableConfigurationProperties(ScrapperHeaders.class)
public class EurojackApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurojackApplication.class, args);
  }

}
