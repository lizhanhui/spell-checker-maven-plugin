package org.apache.maven.plugins.checker;

import org.apache.maven.plugins.checker.core.Suggestion;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaSourceSpellCheckerTest {

    JavaSourceSpellChecker spellChecker = new JavaSourceSpellChecker();
    File file = new File("/Users/macbookpro/IdeaProjects/source-spell-checker/src/main/java/Main.java");

    @Test
    public void testSupport() throws Exception {
        Assert.assertTrue(spellChecker.support(file));
    }

    @Test
    public void testCheck() throws Exception {
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        spellChecker.check(file, suggestions);
        System.out.println(suggestions);
    }
}