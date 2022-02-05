package org.db.assignment3.filter;

import java.util.List;

public class FilterApplication {
    public static void main(String[] args) {
        FiltrationService filtrationService = new FiltrationService();
        List<Article> articles = filtrationService.filterFile();

        new MongoDBConnector().writeToMongo(articles);
    }
}
