package org.db.assignment3.filter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.management.ObjectName;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class MongoDBConnector {

    public static final String DATABASE = "test";
    private MongoDatabase mongoDatabase;

    public MongoDBConnector() {
        MongoClient mongoClient = getMongoClient();
        mongoDatabase = mongoClient.getDatabase(DATABASE);


    }

    private MongoClient getMongoClient() {
        String encodedPassword = URLEncoder.encode("password@123", Charset.defaultCharset());
        ConnectionString connectionString = new ConnectionString("mongodb+srv://dbUser:" + encodedPassword +
                "@cluster0.4yq2y.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    public void writeToMongo(List<Article> articles) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("test123");
        List<Document> documents = getDocuments(articles);
        System.out.println(collection);
        collection.insertMany(documents);
    }

    private List<Document> getDocuments(List<Article> articles) {
        return articles.stream()
                .map(article -> new Document(getFieldValueMap(article)))
                .collect(Collectors.toList());
    }

    private Map<String, Object> getFieldValueMap(Article article) {
        HashMap<String, Object> fieldValueMap = new HashMap<>();
        Arrays.stream(article.getClass().getDeclaredFields())
                .filter(field -> !asList("FIELDS", "source", "articleObjectCreator").contains(field.getName()))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        fieldValueMap.put(field.getName(), field.get(article));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
        Source source = article.getSource();
        Document document = new Document();
        document.append("id", source.getId());
        document.append("name", source.getName());
        fieldValueMap.put("source", document);
        return fieldValueMap;
    }
}
