package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.sql.Date;

@Service
class DrawNumberURIRepository {

  private static final String SAVE_SQL_QUERY =
      "INSERT INTO draw_number_uri(draw_date, detail_uri, archive_url) VALUES(?,?,?);";

  private static final String RETRIEVE_SQL_QUERY =
      "SELECT * FROM draw_number_uri WHERE draw_date=?";

  private final JdbcTemplate jdbcTemplate;

  DrawNumberURIRepository(@Autowired JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.jdbcTemplate.afterPropertiesSet();
  }

  public DrawNumberURI save(DrawNumberURI drawNumberURI) {
    jdbcTemplate.update(SAVE_SQL_QUERY, getSaveQuerySetter(drawNumberURI));
    return jdbcTemplate.query(
        RETRIEVE_SQL_QUERY,
        getRetrieveQuerySetter(drawNumberURI),
        getDrawNumberURIResultMapper());
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
}
