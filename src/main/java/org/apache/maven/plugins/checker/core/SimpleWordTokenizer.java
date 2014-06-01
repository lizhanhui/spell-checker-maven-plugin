/*
 * Copyright 2014 All Rights Reserved.
 */
package org.apache.maven.plugins.checker.core;

/**
 * <p>
 *  <strong>This class is for test purpose only, DO NOT USE IT IN PRODUCTION.</strong>
 * </p>
 *
 * @author Zhanhui Li
 * @version 1.0
 */
public class SimpleWordTokenizer implements WordTokenizer {

    /**
     * The simplest word tokenizer that separates words by space.

     * @param line line of words to tokenize.
     * @return Arrays of words.
     */
    @Override
    public String[] tokenize(String line) {
        if (null == line || line.isEmpty()) {
            throw new IllegalArgumentException("line to tokenize shout not be null or empty");
        }

        return line.split("\\s{1,}");
    }
}
