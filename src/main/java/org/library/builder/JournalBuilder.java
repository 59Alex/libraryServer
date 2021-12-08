package org.library.builder;

import org.library.model.Journal;

public class JournalBuilder extends ContentBuilder<JournalBuilder, Journal> {
    protected JournalBuilder() {
        super(new Journal());
    }

    public static synchronized JournalBuilder init() {
        JournalBuilder builder = new JournalBuilder();
        builder.setBuilder(builder);
        return builder;
    }
}
