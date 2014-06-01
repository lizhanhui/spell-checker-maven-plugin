package org.apache.maven.plugins.checker.core;

import java.util.List;

public class Suggestion {

    public String word;

    public int lineNumber;

    public List<String> suggestedWords;

    public Suggestion(int lineNumber, String word,  List<String> suggestedWords) {
        this.word = word;
        this.lineNumber = lineNumber;
        this.suggestedWords = suggestedWords;
    }
}
