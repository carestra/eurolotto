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

  @Column(name = "balls_draw_order")
  private List<Integer> ballsDrawOrder;
  @Column(name = "euroballs_draw_order")
  private List<Integer> euroBallsDrawOrder;
  @Column(name = "jackpot_amount")
  private BigInteger jackpotAmount;
  @Column(name = "currency_symbol")
  private String currencySymbol;
  private Integer rollover;
  @Column(name = "number_of_jackpot_winners")
  private Integer jackpotWinnersCount;
  @Column(name = "resource_uri")
  private String resourceUri;
  @Column(name = "archive_url")
  private String archiveUrl;

  public DrawDetails convert() {
    EurojackpotDraw draw = new EurojackpotDraw(new BallNumbers(ballsDrawOrder), new EuroBallNumbers(euroBallsDrawOrder));
    JackpotDetail jackpot = new JackpotDetail(new AmountCurrency(jackpotAmount.toString(), currencySymbol), rollover, jackpotWinnersCount);
    return new DrawDetails(draw, jackpot, resourceUri, archiveUrl);
  }
}
