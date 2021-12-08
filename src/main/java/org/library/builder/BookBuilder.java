package org.library.builder;

import org.library.model.Book;

public class BookBuilder extends ContentBuilder<BookBuilder, Book> {

    protected BookBuilder() {
        super(new Book());
    }

    public static synchronized BookBuilder init() {
        BookBuilder builder = new BookBuilder();
        builder.setBuilder(builder);
        return builder;
    }
}
