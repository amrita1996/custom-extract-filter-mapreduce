package org.db.assignment3.mapReduce;

import java.util.List;

import static java.util.Arrays.asList;

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
        articleObjectCreator.setFields(article, FIELDS, this);
        setSource(new Source().from(article));
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
