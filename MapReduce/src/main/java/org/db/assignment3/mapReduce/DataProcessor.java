package org.db.assignment3.mapReduce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.*;

public class DataProcessor {

    public static final int ONE = 1;
    public static final String SPACE = " ";
    public static final String RECORD_PREFIX = "\\{\"source\":";
    public static List<String> words = asList("Canada", "NovaScotia", "education", "higher", "learning", "city",
            "accommodation", "price");
    public static String folderPath = "/Users/amritakrishna/Documents/DAL/WAREHOUSING/Assignments/Assignment 3/newsFiles/";


    public static void displayMinMaxFrequencies() {

        List<File> files = asList(new File(folderPath).listFiles()).stream()
                .filter(file -> file.getName().matches(".*\\.txt")).collect(toList());
        List<WordCollector<String>> mapperOutput = getMapperOutput(files);

        Map<String, List<WordCollector<String>>> groupByWord = groupByWord(mapperOutput);

        Map<String, Integer> frequencyMap = reducer(groupByWord);


        String highest = getMax(frequencyMap);
        String lowest = getMin(frequencyMap);

        System.out.println("Lowest frequency is for key " + lowest + " with frequency " + frequencyMap.get(lowest));
        System.out.println("Highest frequency is for key " + highest + " with frequency " + frequencyMap.get(highest));

        words.stream()
                .filter(word -> !frequencyMap.containsKey(word))
                .forEach(word -> System.out.println(word + " does not exist in any of the files"));
    }

    private static Map<String, Integer> reducer(Map<String, List<WordCollector<String>>> groupByWord) {
        return groupByWord.keySet().stream().collect(toMap(word -> word, word -> groupByWord.get(word).size()));
    }

    private static Map<String, List<WordCollector<String>>> groupByWord(List<WordCollector<String>> mapperOutput) {
        return mapperOutput.stream().collect(groupingBy(WordCollector::getWord));
    }

    private static List<WordCollector<String>> getMapperOutput(List<File> files) {
        return files.stream()
                .map(DataProcessor::getFileContent)
                .flatMap(fileContent -> mapper(fileContent).stream())
                .collect(toList());
    }

    private static List<WordCollector<String>> mapper(String fileContent) {
        return stream(getAllWordsInTitleAndDescription(fileContent))
                .map(word -> new WordCollector<>(word, ONE))
                .filter(wordCollector -> words.contains(wordCollector.getWord()))
                .collect(toList());
    }

    private static String[] getAllWordsInTitleAndDescription(String fileContent) {
        return getRecords(fileContent).stream().map(record -> {
            Article article = new Article().from(record);
            return article.getTitle() + SPACE + article.getContent();
        }).collect(joining(" ")).split(SPACE);
    }

    private static List<String> getRecords(String fileContent) {
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

    private static String getMin(Map<String, Integer> frequencyMap) {
        return frequencyMap.keySet().stream()
                .reduce("", (minValue, currentKey) -> getMinFrequencyKey(frequencyMap, minValue, currentKey));
    }

    private static String getMax(Map<String, Integer> frequencyMap) {
        return frequencyMap.keySet().stream()
                .reduce("", (minValue, currentKey) -> getMaxFrequencyKey(frequencyMap, minValue, currentKey));
    }

    private static String getMinFrequencyKey(Map<String, Integer> frequencyMap, String minValue, String currentKey) {
        return isNull(frequencyMap.get(minValue)) || frequencyMap.get(minValue) > frequencyMap.get(currentKey)
                ? currentKey : minValue;
    }

    private static String getMaxFrequencyKey(Map<String, Integer> frequencyMap, String maxValue, String currentKey) {
        return isNull(frequencyMap.get(maxValue)) || frequencyMap.get(maxValue) < frequencyMap.get(currentKey)
                ? currentKey : maxValue;
    }
}
