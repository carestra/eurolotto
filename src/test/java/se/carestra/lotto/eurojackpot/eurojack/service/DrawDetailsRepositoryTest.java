package se.carestra.lotto.eurojackpot.eurojack.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail.DrawDetailsRepository;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail.DrawResult;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class DrawDetailsRepositoryTest {

  @Autowired
  private DrawDetailsRepository repository;

  private DrawResult drawResult;

  @BeforeEach
  void setUp() {
    drawResult = DrawResult.builder()
        .drawDate(LocalDate.now())
        .ballsDRawOrder(List.of(40,5,27,9,18))
        .euroBallsDRawOrder(List.of(2,11))
        .jackpotAmount(new BigInteger("300000"))
        .currencySymbol("kr")
        .rollover(0)
        .jackpotWinnersCount(0)
        .archiveUrl("htt://lotto.mock.dot/results/15-06-2020")
        .build();
  }

  @Test
  void saveOneDrawDetails() {
    DrawResult saved = repository.save(drawResult);

    assertNotNull(saved);
    assertEquals(drawResult.getDrawDate(), saved.getDrawDate());
  }
}