package se.carestra.lotto.eurojackpot.eurojack.drawdetail.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class DrawDetailsRepositoryTest {

  @Autowired
  private DrawDetailsRepository repository;

  private DrawResultDetail drawResultDetail;

  @BeforeEach
  void setUp() {
    drawResultDetail = DrawResultDetail.builder()
        .drawDate(LocalDate.now())
        .ballsDrawOrder(List.of(40,5,27,9,18))
        .euroBallsDrawOrder(List.of(2,11))
        .jackpotAmount(new BigInteger("300000"))
        .currencySymbol("kr")
        .rollover(0)
        .jackpotWinnersCount(0)
        .archiveUrl("htt://lotto.mock.dot/results/15-06-2020")
        .build();
  }

  @Test
  void saveOneDrawDetails() {
    DrawResultDetail saved = repository.save(drawResultDetail);

    assertNotNull(saved);
    assertEquals(drawResultDetail.getDrawDate(), saved.getDrawDate());
  }
}