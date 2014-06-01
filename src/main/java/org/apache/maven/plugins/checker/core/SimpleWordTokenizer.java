/*
 * Copyright 2014 All Rights Reserved.
 */
package org.apache.maven.plugins.checker.core;

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

    private static Pattern WORD_PATTERN = Pattern.compile("\\w{1,}");

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
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
    }
}
