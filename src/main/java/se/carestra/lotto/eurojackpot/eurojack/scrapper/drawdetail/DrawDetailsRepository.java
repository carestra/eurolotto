package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DrawDetailsRepository extends CrudRepository<DrawResult, LocalDate> {
}
