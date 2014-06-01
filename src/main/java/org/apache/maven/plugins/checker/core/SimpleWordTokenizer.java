/*
 * Copyright 2014 All Rights Reserved.
 */
package org.apache.maven.plugins.checker.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *  <strong>This class is for test purpose only, DO NOT USE IT IN PRODUCTION.</strong>
 * </p>
 *
 * @author Zhanhui Li
 * @version 1.0
 */
public class SimpleWordTokenizer implements WordTokenizer {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private static Pattern WORD_PATTERN = Pattern.compile("\\w{1,}");

    private static StringBuffer BUFFER = new StringBuffer(DEFAULT_BUFFER_SIZE);

    /**
     * The simplest word tokenizer that separates words by space.

     * @param line line of words to tokenize.
     * @param words  List of words.
     */
    @Override
    public void tokenize(String line, List<String> words) {
        if (null == line || line.isEmpty()) {
            throw new IllegalArgumentException("line to tokenize shout not be null or empty");
        }
        Matcher matcher = WORD_PATTERN.matcher(line);
        String extractedWord = null;
        List<String> parsedWords = new ArrayList<String>();
        while (matcher.find()) {
            extractedWord = matcher.group(0);
            if (!isCompoundWord(extractedWord)) {
                words.add(extractedWord);
            } else {
                parseCompoundWord(extractedWord, parsedWords);
                words.addAll(parsedWords);
            }
        }
        words.remove("");
    }


    public static void parseCompoundWord(String compoundWord, List<String> words) {
        words.clear();

        //handle UPPER_CASE_NAMES
        if (compoundWord.equals(compoundWord.toUpperCase())) {
            words.addAll(Arrays.asList(compoundWord.split("_")));
            return;
        }

        BUFFER.delete(0, BUFFER.length());
        final int len = compoundWord.length();
        int i = 0, j = 0;
        char c = ' ';
        for (; i < len; i++) {
            if (i == len - 1) {
                words.add(compoundWord.substring(j));
                break;
            }

            c = compoundWord.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                if (i > j) {
                    words.add(compoundWord.substring(j, i));
                    j = i;
                }
            }
        }
    }

    public static boolean isCompoundWord(String extractedWord) {
        if (extractedWord.equals(extractedWord.toLowerCase())) {
            return false;
        }

        if (extractedWord.equals(extractedWord.toUpperCase()) && !extractedWord.contains("_")) {
            return false;
        }

        return true;
    }
}
