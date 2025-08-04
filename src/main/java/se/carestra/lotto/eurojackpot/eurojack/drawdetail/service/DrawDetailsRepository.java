package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DrawDetailsRepository extends CrudRepository<DrawResultDetail, String> {
  Optional<DrawResultDetail> findByDrawDate(LocalDate drawDate);
}
