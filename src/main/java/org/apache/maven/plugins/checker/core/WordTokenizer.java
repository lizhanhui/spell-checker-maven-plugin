package org.apache.maven.plugins.checker.core;

import java.util.List;

public interface WordTokenizer {

    /**
     * This method intends to split a line into separate human readable words.
     * @param line A line of words.
     * @param words  Separate human readable words in form of list.
     */
    void tokenize(String line, List<String> words);
}
