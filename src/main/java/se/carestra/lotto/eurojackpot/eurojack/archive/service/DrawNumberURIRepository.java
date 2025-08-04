package se.carestra.lotto.eurojackpot.eurojack.archive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class DrawNumberURIRepository {

  private static final String SAVE_SQL_QUERY =
      "INSERT INTO draw_resource_uri(draw_date, resource_uri, archive_url) VALUES(?,?,?);";

  private static final String EXIST_DATE_SQL_QUERY =
      "SELECT EXISTS(SELECT * FROM draw_resource_uri where EXTRACT(YEAR FROM draw_date)=?);";

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

  public boolean hasArchiveYear(String year) {
    return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXIST_DATE_SQL_QUERY, Boolean.class, Integer.parseInt(year)));
  }

  public Optional<List<DrawNumberURI>> fetch(String year) {
    return jdbcTemplate.query(
        "SELECT * FROM draw_resource_uri where EXTRACT(YEAR FROM draw_date)=?;",
        (PreparedStatement ps) -> ps.setInt(1, Integer.parseInt(year)),
        rs -> {
          if (rs.next()) {
            List<DrawNumberURI> uris = new ArrayList<>();

            while (rs.next()) {
              uris.add(new DrawNumberURI(rs.getString("resource_uri"), rs.getString("archive_url")));
            }

            return Optional.of(uris);
          } else {
            return Optional.empty();
          }
        }
    );
  }

  public Optional<DrawNumberURI> findById(LocalDate localDate) {
    return jdbcTemplate.query(
        "SELECT * FROM draw_resource_uri where draw_date=?;",
        (PreparedStatement ps) -> ps.setDate(1, Date.valueOf(localDate)),
        (ResultSet re) -> {
          if (re.next()) {
            return Optional.of(new DrawNumberURI(re.getString("resource_uri"), re.getString("archive_url")));
          } else {
            return Optional.empty();
          }
        }
    );
  }
}
