package org.db.assignment3.mapReduce;

import java.util.List;

import static java.util.Arrays.asList;

public class Source {
    private final static List<String> FIELDS = asList("id", "name");
    private final ObjectCreator<Source> sourceObjectCreator = new ObjectCreator<>();
    private String id;
    private String name;

    public Source() {
    }

    public Source from(String article) {
        sourceObjectCreator.setFields(article, FIELDS, this);
        return this;
    }

}
