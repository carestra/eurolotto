package se.carestra.lotto.eurojackpot.eurojack.drawdetail.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DrawDetailsRepositoryTest {

  @Autowired
  private DrawDetailsRepository repository;

  private DrawResultDetail drawResultDetail;

  private final LocalDate drawDate = LocalDate.now();
  private String resourceUri;

  @BeforeEach
  void setUp() {
    resourceUri = "/results/" + drawDate.format(DrawNumberURI.DATE_TEXT_FORMATTER);

    drawResultDetail = DrawResultDetail.builder()
        .drawDate(drawDate)
        .selectedBallsDrawOrder(List.of(40, 5, 27, 9, 18))
        .euroBallsDrawOrder(List.of(2, 11))
        .jackpotAmount(new BigInteger("300000"))
        .currencySymbol("kr")
        .rollover(0)
        .jackpotWinnersCount(0)
        .resourceUri(resourceUri)
        .archiveUrl("htt://lotto.mock.dot/results/15-06-2020")
        .build();
  }

  @Test
  void saveOneDrawDetails() {
    DrawResultDetail saved = repository.save(drawResultDetail);

    assertNotNull(saved);
    assertEquals(drawResultDetail.getDrawDate(), saved.getDrawDate());
    assertEquals(drawResultDetail.getResourceUri(), saved.getResourceUri());
  }

  @Test
  void findById() {
    DrawResultDetail saved = repository.save(drawResultDetail);
    assertNotNull(saved);

    Optional<DrawResultDetail> detailOptional = repository.findById(resourceUri);

    assertTrue(detailOptional.isPresent());
    DrawResultDetail foundDetail = detailOptional.get();
    assertEquals(drawResultDetail.getResourceUri(), foundDetail.getResourceUri());
    assertEquals(drawResultDetail.getDrawDate(), foundDetail.getDrawDate());
    assertEquals(drawResultDetail.getSelectedBallsDrawOrder(), foundDetail.getSelectedBallsDrawOrder());
    assertEquals(drawResultDetail.getEuroBallsDrawOrder(), foundDetail.getEuroBallsDrawOrder());
  }

  @Test
  void notFoundById() {
    DrawResultDetail saved = repository.save(drawResultDetail);
    assertNotNull(saved);

    LocalDate drawDate = LocalDate.now().plusDays(2);
    Optional<DrawResultDetail> detailOptional = repository.findById("/results/" + drawDate.format(DrawNumberURI.DATE_TEXT_FORMATTER));

    assertFalse(detailOptional.isPresent());
  }

  @Test
  void foundByDrawDate() {
    DrawResultDetail saved = repository.save(drawResultDetail);
    assertNotNull(saved);
    assertEquals(drawResultDetail.getDrawDate(), saved.getDrawDate());
  }
}