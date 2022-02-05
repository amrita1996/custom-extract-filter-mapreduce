package org.db.assignment3.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class FiltrationService {
    public static final String RECORD_PREFIX = "\\{\"source\":";
    public static String folderPath = "/Users/amritakrishna/Documents/DAL/WAREHOUSING/Assignments/Assignment 3/newsFiles/";

    public List<Article> filterFile() {
        List<File> files = asList(new File(folderPath).listFiles());
        List<String> fileContents = files.stream().map(FiltrationService::getFileContent).collect(toList());
        List<Article> articles = fileContents.stream()
                .flatMap(fileContent -> getEachRecord(fileContent).stream())
                .map(eachContent -> new Article().from(eachContent)).collect(toList());
        return articles;
    }

    private List<String> getEachRecord(String fileContent) {
        String[] split = fileContent.split(RECORD_PREFIX);
        return Arrays.stream(split)
                .filter(eachRecord -> !eachRecord.isEmpty())
                .map(eachRecord -> RECORD_PREFIX + eachRecord)
                .collect(toList());
    }


    private static String getFileContent(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String fileContent = bufferedReader.lines().collect(Collectors.joining());
            bufferedReader.close();
            return fileContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
