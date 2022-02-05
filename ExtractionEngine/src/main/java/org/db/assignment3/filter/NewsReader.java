package org.db.assignment3.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class NewsReader {
    public static final int ONE = 1;
    public static final String SOURCE_PREFIX = "{\"source\":";
    public static final String SOURCE_REGEX = "\\{\"source\":";
    public static final String ARRAY_REGEX = "\\[";
    public static final String NEW_LINE = "\n";
    public static final String TXT = ".txt";
    public static final String HYPHEN = "-";
    public static final String SPACE = " ";
    public static final String ESCAPE_SPACE = "%20";
    public static final int ZERO = 0;
    public static String url = "https://newsapi.org/v2/everything?pageSize=100&q=";
    public static String apiKey = "f308bdc7f41048c6b295daf72ffb9124";
    public static List<String> searchTerms = asList("Canada", "University", "Halifax",
            "Canada Education", "Moncton", "Toronto");
    public static String folderPath = "/newsFiles/";

    public static void saveNews() {
        searchTerms.forEach(NewsReader::saveNewsForKeyword);
    }

    private static void saveNewsForKeyword(String keyword) {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(
                URI.create(url + keyword.replace(SPACE, ESCAPE_SPACE)))
                .GET()
                .header("accept", "application/json")
                .header("X-Api-Key", apiKey)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(UTF_8));
            List<String> strings = splitArticles(5, response.body());
            writeToFiles(strings, keyword);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFiles(List<String> strings, String keyword) {
        strings.forEach(string -> {
            try {
                FileWriter fileWriter = new FileWriter(folderPath + keyword + HYPHEN +
                        (strings.indexOf(string)+1) + TXT);
                fileWriter.write(string);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private static List<String> splitArticles(Integer splitSize, String responseBody) {
        String[] content = responseBody.split(ARRAY_REGEX, 2);
        String[] articlesArray = content[1].split(SOURCE_REGEX);
        List<String> articles = stream(articlesArray, ONE, articlesArray.length)
                .map(entry -> SOURCE_PREFIX + entry)
                .collect(toList());

        return IntStream.rangeClosed(ZERO, getLastIndex(articles, splitSize))
                .mapToObj(i -> {
                    int fromIndex = i * splitSize;
                    int toIndex = (i + 1) * splitSize;
                    return articles.subList(fromIndex, Math.min(toIndex, articles.size()));
                })
                .map(NewsReader::getFileContent)
                .collect(toList());
    }

    private static String getFileContent(List<String> strings) {
        return String.join(NEW_LINE, strings);
    }

    private static int getLastIndex(List<String> articles, int count) {
        int dividedByCount = articles.size() / count;
        return dividedByCount * count < articles.size() ? dividedByCount : dividedByCount - 1;
    }
}
