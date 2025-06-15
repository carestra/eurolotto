package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "draw_result")
public class DrawResult {
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
  private List<Integer> ballsDRawOrder;
  @Column(name = "euroballs_draw_order")
  private List<Integer> euroBallsDRawOrder;
  @Column(name = "jackpot_amount")
  private BigInteger jackpotAmount;
  @Column(name = "currency_symbol")
  private String currencySymbol;
  private Integer rollover;
  @Column(name = "number_of_jackpot_winners")
  private Integer jackpotWinnersCount;
  @Column(name = "archive_url")
  private String archiveUrl;

}
