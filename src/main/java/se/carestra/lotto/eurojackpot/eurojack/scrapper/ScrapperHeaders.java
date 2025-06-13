package se.carestra.lotto.eurojackpot.eurojack.scrapper;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scrapper.headers")
public record ScrapperHeaders(String userAgent, String acceptLang, String acceptLangValue) {
}
