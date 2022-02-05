package org.db.assignment3.mapReduce;

public class WordCollector<W> {
    private final W word;
    private final Integer frequency;

    public WordCollector(W word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public W getWord() {
        return word;
    }

    public Integer getFrequency() {
        return frequency;
    }
}
