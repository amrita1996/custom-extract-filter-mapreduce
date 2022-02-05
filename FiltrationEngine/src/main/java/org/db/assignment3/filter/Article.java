package org.db.assignment3.filter;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

public class Article {
    public static final List<String> FIELDS = asList("source",
            "author",
            "title",
            "description",
            "url",
            "urlToImage",
            "publishedAt",
            "content");
    private ObjectCreator<Article> articleObjectCreator = new ObjectCreator<>();
    private Source source;
    private String author;
    private String title;
    private String description;
    private String publishedAt;
    private String content;

    public Article from(String article) {
        articleObjectCreator.setCleanFields(article, FIELDS, this);
        setSource(new Source().from(article));
        return this;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
