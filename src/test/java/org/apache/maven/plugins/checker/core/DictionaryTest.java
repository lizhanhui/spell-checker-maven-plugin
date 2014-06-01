package org.apache.maven.plugins.checker.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DictionaryTest {

    private Dictionary dictionary = Dictionary.getInstance();

    @Test
    public void testIsWord() {
        Assert.assertTrue(dictionary.isWord("parameter"));
    }

    @Test
    public void testSuggest() {
        long start = System.currentTimeMillis();
        List<String> suggestion = dictionary.suggest("father");
        System.out.println("Time Used:" + (System.currentTimeMillis() - start) + " ms");
        System.out.println("Suggestion: " + suggestion);

    }

}