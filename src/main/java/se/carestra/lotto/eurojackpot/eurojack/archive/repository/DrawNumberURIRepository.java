package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Service
public class DrawNumberURIRepository {

  private static final String SAVE_SQL_QUERY =
      "INSERT INTO draw_number_uri(draw_date, detail_uri, archive_url) VALUES(?,?,?);";

  private static final String RETRIEVE_SQL_QUERY =
      "SELECT * FROM draw_number_uri WHERE draw_date=?";

  private static final String EXIST_DATE_SQL_QUERY =
      "SELECT EXISTS(SELECT * FROM draw_number_uri where EXTRACT(YEAR FROM draw_date)=?)";

  private static final int BATCH_SIZE = 26;

  private final JdbcTemplate jdbcTemplate;

  public DrawNumberURIRepository(@Autowired JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcTemplate.afterPropertiesSet();
  }

  public void saveAll(List<DrawNumberURI> uris) {
    jdbcTemplate.batchUpdate(
        SAVE_SQL_QUERY,
        uris,
        BATCH_SIZE,
        (PreparedStatement ps, DrawNumberURI drawNumber) -> {
          ps.setDate(1, Date.valueOf(drawNumber.drawDate()));
          ps.setString(2, drawNumber.resourceUri());
          ps.setString(3, drawNumber.archiveUrl());
        }
    );
  }

  public DrawNumberURI save(DrawNumberURI drawNumberURI) {
    jdbcTemplate.update(SAVE_SQL_QUERY, getSaveQuerySetter(drawNumberURI));
    return jdbcTemplate.query(
        RETRIEVE_SQL_QUERY,
        getRetrieveQuerySetter(drawNumberURI),
        getDrawNumberURIResultMapper()
    );
  }

  private static ResultSetExtractor<DrawNumberURI> getDrawNumberURIResultMapper() {
    return rs -> {
      if (rs.next()) {
        return new DrawNumberURI(rs.getString("detail_uri"), rs.getString("archive_url"));
      }
      return null;
    };
  }

  private static PreparedStatementSetter getRetrieveQuerySetter(DrawNumberURI drawNumber) {
    return ps -> ps.setDate(1, Date.valueOf(drawNumber.drawDate()));
  }

  private static PreparedStatementSetter getSaveQuerySetter(DrawNumberURI drawNumber) {
    return ps -> {
      ps.setDate(1, Date.valueOf(drawNumber.drawDate()));
      ps.setString(2, drawNumber.resourceUri());
      ps.setString(3, drawNumber.archiveUrl());
    };
  }

  public boolean hasArchiveYear(int year) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_DATE_SQL_QUERY, Boolean.class, year));
  }
}
