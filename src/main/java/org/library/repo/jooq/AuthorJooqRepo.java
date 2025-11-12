package org.library.repo.jooq;

import com.example.jooq.tables.Authors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AuthorJooqRepo {
    private final DSLContext dsl;

    public Map<Integer, Integer> countAuthorsByBirthYear() {
        Authors a = Authors.AUTHORS;

        return dsl.select(DSL.extract(a.BIRTH_DATE, DatePart.YEAR).as("year"), DSL.count())
                .from(a)
                .groupBy(DSL.extract(a.BIRTH_DATE, DatePart.YEAR))
                .fetchMap(r -> r.get("year", Integer.class), r -> r.get(1, Integer.class));
    }
}
