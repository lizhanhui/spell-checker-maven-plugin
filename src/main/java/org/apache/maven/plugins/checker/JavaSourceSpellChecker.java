package org.apache.maven.plugins.checker;

import org.apache.maven.plugins.checker.core.Dictionary;
import org.apache.maven.plugins.checker.core.SimpleWordTokenizer;
import org.apache.maven.plugins.checker.core.Suggestion;
import org.apache.maven.plugins.checker.core.WordTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     This file implements feature of checking java source code.
 * </p>
 *
 * <p>
 *     <strong>Thread Safety:</strong> This class is thread safe.
 * </p>
 * @author Zhanhui Li
 * @version 1.0
 */
public class JavaSourceSpellChecker extends SpellCheckerAdapter {

    private WordTokenizer wordTokenizer;

    private Dictionary dictionary;

    public JavaSourceSpellChecker() {
        wordTokenizer = new SimpleWordTokenizer();
        dictionary = Dictionary.getInstance();
    }

    /**
     * This method determines if the given file is java source file.
     * @param file File to check.
     * @return true if supported; false otherwise.
     * @throws IllegalArgumentException if <code>file</code> is invalid.
     */
    @Override
    public boolean support(File file) {
        if (null == file || !file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File to check is invalid");
        }
        return file.getName().toLowerCase().endsWith("java");
    }

    /**
     * This method checks the specified java source file and return a list of {@link Suggestion} if typos were detected.
     * @param file File to check.
     * @param suggestions  list of {@link Suggestion} corresponding to typos.
     */
    @Override
    public void check(File file, List<Suggestion> suggestions) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            int lineNum = 0;
            List<String> words = new ArrayList<String>();
            while (null != (line = bufferedReader.readLine())) {
                lineNum++;
                if (!line.trim().isEmpty()) {
                    wordTokenizer.tokenize(line, words);
                    for (String word : words) {
                        if (!dictionary.isWord(word.toLowerCase())) {
                            suggestions.add(new Suggestion(lineNum, word, dictionary.suggest(word)));
                        }
                    }
                    words.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                //ignore.
            }
        }
    }
}
