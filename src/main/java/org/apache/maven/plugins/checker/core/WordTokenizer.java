package org.apache.maven.plugins.checker.core;

public interface WordTokenizer {

    /**
     * This method intends to split a line into separate human readable words.
     * @param line A line of words.
     * @return Separate human readable words in form of array.
     */
    String[] tokenize(String line);
}
