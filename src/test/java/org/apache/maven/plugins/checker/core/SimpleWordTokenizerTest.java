package org.apache.maven.plugins.checker.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SimpleWordTokenizerTest {

    @Test
    public void testParseCompoundWord() throws Exception {
        List<String> words = new ArrayList<String>();
        SimpleWordTokenizer.parseCompoundWord("goodName", words);
        System.out.println(words);
    }
}