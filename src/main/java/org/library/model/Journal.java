package org.library.model;

import org.library.builder.JournalBuilder;
import org.library.builder.NewspaperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Journal extends Content {
    private final ContentType journal = ContentType.JOURNAL;

    public Journal(Long id, String description, String name, LocalDate date, String body, Author author) {
        super(id, description, name, date, body, author);
    }

    public Journal() {
    }

    public static synchronized JournalBuilder createBuilder() {
        return JournalBuilder.init();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Journal journal1 = (Journal) o;
        return journal == journal1.journal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), journal);
    }
}
