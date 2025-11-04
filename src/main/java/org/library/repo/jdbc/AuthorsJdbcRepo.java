package org.library.repo.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AuthorsJdbcRepo {
    private final JdbcTemplate jdbcTemplate;

    public Map<Integer, Integer> countAuthorsByBirthYear() {
        String sql = "SELECT EXTRACT(YEAR FROM birth_date) AS year, COUNT(*) FROM authors GROUP BY year";

        return jdbcTemplate.query(sql, resultSet -> {
            Map<Integer, Integer> result = new HashMap<>();
            while (resultSet.next()) {
                int year = resultSet.getInt("year");
                int count = resultSet.getInt("count");
                result.put(year, count);
            }
            return result;
        });
    }
}
