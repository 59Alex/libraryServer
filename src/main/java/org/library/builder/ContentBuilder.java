package org.library.builder;

import org.library.model.Author;
import org.library.model.Content;
import org.library.model.Newspaper;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ContentBuilder<B,V extends Content> {
    private V body;
    private B builder;

    protected ContentBuilder(V body) {
        this.body = body;
    }
    protected void setBuilder(B builder) {
        this.builder = builder;
    }

    public B setId(Long id) {
        this.body.setId(id);
        return this.builder;
    }

    public B setDescription(String description) {
        this.body.setDescription(description);
        return this.builder;
    }

    public B setName(String name) {
        this.body.setName(name);
        return this.builder;
    }

    public B setBody(String body) {
        this.body.setBody(body);
        return this.builder;
    }

    public B setDate(LocalDate date) {
        this.body.setDate(date);
        return this.builder;
    }

    public B setAuthor(Author author) {
        this.body.setAuthor(author);
        return this.builder;
    }

    public V build() {
        return this.body;
    }
}
