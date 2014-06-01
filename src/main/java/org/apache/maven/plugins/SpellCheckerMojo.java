package org.apache.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.checker.JavaSourceSpellChecker;
import org.apache.maven.plugins.checker.SpellChecker;
import org.apache.maven.plugins.checker.core.Suggestion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal to check spelling of source code.
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpellCheckerMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = false)
    private File sourceDirectory;

    private static SpellChecker[] SPELL_CHECKERS = {new JavaSourceSpellChecker()};

    public void execute() throws MojoExecutionException {
        if (null == sourceDirectory || !sourceDirectory.exists()) {
            getLog().warn("Make sure you are specifying the correct source directory");
        }
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        checkFile(sourceDirectory, suggestions);

    }


    private void checkFile(File file, List<Suggestion> suggestions) {

        if (file.isFile()) {
            for (SpellChecker spellChecker : SPELL_CHECKERS) {
                if (spellChecker.support(file)) {
                    spellChecker.check(file, suggestions);
                    for (Suggestion suggestion : suggestions) {
                        getLog().warn("Line No." + suggestion.lineNumber + ": " + suggestion.word
                                + " --> " + suggestion.suggestedWords);
                    }
                    suggestions.clear();
                    break;
                }
            }
        } else {
            File[] subDirectories = file.listFiles();
            for (File f : subDirectories) {
                checkFile(f, suggestions);
            }
        }
    }
}
