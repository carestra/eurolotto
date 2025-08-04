package se.carestra.lotto.eurojackpot.eurojack.drawdetail.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "draw_detail")
public class DrawResultDetail {
  @Id
  @Column(name = "resource_uri")
  private String resourceUri;

  @Column(name = "draw_date")
  private LocalDate drawDate;

  @Version
  private Long version;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  @Temporal(TIMESTAMP)
  private LocalDateTime createdDate;

  @JsonIgnore
  @Column(name = "modified_date")
  @LastModifiedDate
  private LocalDateTime modifiedDate;

  @Column(name = "selected_balls_draw_order")
  private List<Integer> selectedBallsDrawOrder;
  @Column(name = "euro_balls_draw_order")
  private List<Integer> euroBallsDrawOrder;
  @Column(name = "jackpot_amount")
  private BigInteger jackpotAmount;
  @Column(name = "currency_symbol")
  private String currencySymbol;
  @Column(name = "jackpot_rollover_count")
  private Integer rollover;
  @Column(name = "number_of_jackpot_winners")
  private Integer jackpotWinnersCount;
  @Column(name = "archive_url")
  private String archiveUrl;

  public DrawDetails convert() {
    SelectedBallNumbers selectedBallNumbers = new SelectedBallNumbers(selectedBallsDrawOrder);
    EuroBallNumbers euroBallNumbers = new EuroBallNumbers(euroBallsDrawOrder);
    JackpotAmount amount = new JackpotAmount(jackpotAmount, currencySymbol);
    JackpotRollover rolloverCount = new JackpotRollover(rollover);
    JackpotNumberOfWinners winners = new JackpotNumberOfWinners(jackpotWinnersCount);
    return new DrawDetails(selectedBallNumbers, euroBallNumbers, amount, rolloverCount, winners, resourceUri, archiveUrl);
  }

  public static DrawResultDetail convert(DrawDetails drawDetail) {
    DrawResultDetailBuilder builder = DrawResultDetail.builder();
    builder.resourceUri(drawDetail.resourceUri());
    builder.drawDate(drawDetail.drawDate());
    builder.selectedBallsDrawOrder(drawDetail.selectedBallNumbers().numbers());
    builder.euroBallsDrawOrder(drawDetail.euroBallNumbers().numbers());
    builder.jackpotAmount(drawDetail.jackpotAmount().amountAsBigInteger());
    builder.currencySymbol(drawDetail.jackpotAmount().currencySymbol());
    builder.rollover(drawDetail.jackpotRollover().rollover());
    builder.jackpotWinnersCount(drawDetail.jackpotNumberOfWinners().nrOfWinners());
    builder.archiveUrl(drawDetail.fullPath());
    return builder.build();
  }
}
