package org.library.builder;

import org.library.model.Author;

public class AuthorBuilder {

    private Author body;
    private AuthorBuilder builder;

    private AuthorBuilder() {
    }

    public static synchronized AuthorBuilder init() {
        AuthorBuilder builder = new AuthorBuilder();
        builder.setAuthorBuilder(builder);
        return builder;
    }
    public void setAuthorBuilder(AuthorBuilder builder) {
        this.builder = builder;
    }

    public AuthorBuilder setId(Long id) {
        this.body.setId(id);
        return this.builder;
    }

    public AuthorBuilder setFirstName(String firstName) {
        this.body.setFirstName(firstName);
        return this.builder;
    }

    public AuthorBuilder setLastName(String lastName) {
        this.body.setLastName(lastName);
        return this.builder;
    }

    public AuthorBuilder setDescription(String description) {
        this.body.setDescription(description);
        return this.builder;
    }

    public Author build() {
        return this.body;
    }
}
