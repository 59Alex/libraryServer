package org.library.builder;

import org.library.model.Newspaper;

public class NewspaperBuilder extends ContentBuilder<NewspaperBuilder, Newspaper> {
    private Newspaper newspaper;
    private NewspaperBuilder builder;

    private NewspaperBuilder() {
        super(new Newspaper());
    }

    public static synchronized NewspaperBuilder init() {
        NewspaperBuilder builder = new NewspaperBuilder();
        builder.setBuilder(builder);
        return builder;
    }




}
