package org.apache.maven.plugins.checker;

import org.apache.maven.plugins.checker.core.Suggestion;

import java.io.File;
import java.util.List;

public class SpellCheckerAdapter implements SpellChecker {

    /**
     * This method determines if this checker is capable of checking given file.
     * @param file File to check.
     * @return true if supported; false otherwise.
     * @throws IllegalArgumentException if <code>file</code> is invalid.
     */
    @Override
    public boolean support(File file) {
        return false;
    }

    /**
     * This method checks the specified file and return a list of {@link Suggestion} if typos were detected.
     * @param file File to check.
     * @param suggestions  list of {@link Suggestion} corresponding to typos.
     */
    @Override
    public void check(File file, List<Suggestion> suggestions) {
    }
}
