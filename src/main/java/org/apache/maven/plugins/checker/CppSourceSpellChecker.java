package org.apache.maven.plugins.checker;

import org.apache.maven.plugins.checker.core.Suggestion;

import java.io.File;
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
public class CppSourceSpellChecker extends SpellCheckerAdapter {

    /**
     * This method determines if the given file is cpp source file.
     * @param file File to check.
     * @return true if supported; false otherwise.
     * @throws IllegalArgumentException if <code>file</code> is invalid.
     */
    @Override
    public boolean support(File file) {
        if (null == file || !file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("File to check is invalid");
        }

        return super.support(file);
    }

    /**
     * This method checks the specified cpp source file and return a list of {@link Suggestion} if typos were detected.
     * @param file File to check.
     * @param suggestions  list of {@link Suggestion} corresponding to typos.
     */
    @Override
    public void check(File file, List<Suggestion> suggestions) {
    }
}
