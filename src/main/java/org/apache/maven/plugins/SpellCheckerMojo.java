package org.apache.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.checker.JavaSourceSpellChecker;
import org.apache.maven.plugins.checker.SpellChecker;
import org.apache.maven.plugins.checker.core.Suggestion;
import org.apache.maven.plugins.util.PathUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal to check spelling of source code.
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SpellCheckerMojo extends AbstractMojo {

    /**
     * Location of the source directory.
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = false)
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}", property = "targetDirectory", required = false)
    private File targetDirectory;

    private static SpellChecker[] SPELL_CHECKERS = {new JavaSourceSpellChecker()};

    public void execute() throws MojoExecutionException {
        if (null == sourceDirectory || !sourceDirectory.exists()) {
            getLog().warn("Make sure you are specifying the correct source directory");
        }
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        BufferedWriter bufferedWriter = null;
        try {
            File spellReportFile = new File(targetDirectory, "spelling_check.txt");
            if (!spellReportFile.exists()) {
                spellReportFile.createNewFile();
            }
            bufferedWriter = new BufferedWriter(new FileWriter(spellReportFile));
            checkFile(sourceDirectory, suggestions, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedWriter) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    //ignore.
                }
            }
        }
    }


    private void checkFile(File file, List<Suggestion> suggestions, BufferedWriter bufferedWriter) throws IOException {

        if (file.isFile()) {
            String relativePath = PathUtil.getRelativePath(sourceDirectory, file);
            getLog().info("Check Spelling: " + relativePath);
            boolean checked = false;
            for (SpellChecker spellChecker : SPELL_CHECKERS) {
                if (spellChecker.support(file)) {
                    spellChecker.check(file, suggestions);
                    for (Suggestion suggestion : suggestions) {
                        getLog().warn("Possible Spell Error at Line No." + suggestion.lineNumber + ":[" + suggestion.word
                                + "] Suggested Spelling: " + suggestion.suggestedWords);
                        bufferedWriter.write("File: " + relativePath
                                + "   Line: " + suggestion.lineNumber
                                + "Spelling Error: " + suggestion.word
                                + "   Suggestion: " + suggestion.suggestedWords);
                        bufferedWriter.newLine();
                    }
                    suggestions.clear();
                    checked = true;
                    break;
                }
            }
            if (!checked) {
                getLog().warn("Unable to check " + relativePath);
            }
        } else {
            File[] subDirectories = file.listFiles();
            for (File f : subDirectories) {
                checkFile(f, suggestions, bufferedWriter);
            }
        }
    }
}
