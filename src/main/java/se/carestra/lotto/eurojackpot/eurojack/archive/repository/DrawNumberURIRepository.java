package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DrawNumberURIRepository {

  private static final String SAVE_SQL_QUERY =
      "INSERT INTO draw_resource_uri(draw_date, resource_uri, archive_url) VALUES(?,?,?);";

  private static final String RETRIEVE_SQL_QUERY =
      "SELECT * FROM draw_resource_uri WHERE draw_date=?;";

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

  public DrawNumberURI save(DrawNumberURI drawNumberURI) {
    jdbcTemplate.update(
        SAVE_SQL_QUERY,
        ps1 -> {
          ps1.setDate(1, Date.valueOf(drawNumberURI.drawDate()));
          ps1.setString(2, drawNumberURI.resourceUri());
          ps1.setString(3, drawNumberURI.archiveUrl());
        }
    );

    return jdbcTemplate.query(
        RETRIEVE_SQL_QUERY,
        ps -> ps.setDate(1, Date.valueOf(drawNumberURI.drawDate())),
        rs -> {
          if (rs.next()) {
            return new DrawNumberURI(rs.getString("resource_uri"), rs.getString("archive_url"));
          }
          return null;
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

}
