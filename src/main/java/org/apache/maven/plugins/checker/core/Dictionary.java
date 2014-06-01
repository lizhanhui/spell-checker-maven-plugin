package org.apache.maven.plugins.checker.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dictionary {

    private static int MAX_EDIT_DISTANCE = 3;

    private static final int MAX_WORD_DIFFERENCE_IN_LENGTH = 3;

    private static int MAX_RESULT = 10;

    private static HashMap<Integer, Set<String>> WORDS = new HashMap<Integer, Set<String>>();

    private static HashMap<String, List<String>> RESULTS = new HashMap<String, List<String>>();

    private static List<String> WORD_FREQUENCY = new ArrayList<String>();

    private Dictionary() {
        loadDictionary();
        loadWordFrequency();
    }

    /**
     * Load dictionary.
     */
    private void loadDictionary() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            inputStream = Dictionary.class.getClassLoader().getResourceAsStream("en_us.dic");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (null != (line = bufferedReader.readLine())) {
                line = line.trim();
                if (null == WORDS.get(line.length())) {
                    Set<String> wordSet = new HashSet<String>();
                    wordSet.add(line);
                    WORDS.put(line.length(), wordSet);
                } else {
                    WORDS.get(line.length()).add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }

                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                //ignore.
            }
        }
    }

    /**
     * Load word frequency.
     */
    private void loadWordFrequency() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            inputStream = Dictionary.class.getClassLoader().getResourceAsStream("frequency.dict");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (null != (line = bufferedReader.readLine())) {
                line = line.trim();
                if (!WORD_FREQUENCY.contains(line)) {
                    WORD_FREQUENCY.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }

                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                //ignore.
            }
        }
    }

    private static final class DictionaryHolder {
        private static Dictionary INSTANCE = new Dictionary();
    }

    public static List<String> suggest(String word) {
        if (RESULTS.containsKey(word)) {
            return RESULTS.get(word);
        }

        Map<Integer, List<String>> results = suggest(word, Math.min(MAX_WORD_DIFFERENCE_IN_LENGTH, word.length() / 2));
        List<String> result = new ArrayList<String>();
        result.addAll(results.get(0));
        List<String> row = null;
        for (int i = 1; i <= MAX_EDIT_DISTANCE; i++) {
            row = results.get(i);
            if (!row.isEmpty()) {
                sort(row);
                for (String rankedWord : row) {
                    if (result.size() < MAX_RESULT) {
                        result.add(rankedWord);
                    }
                }
            }
        }
        //sort(result);
        RESULTS.put(word, result);
        return result;
    }

    private static void sort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (!WORD_FREQUENCY.contains(a) && !WORD_FREQUENCY.contains(b)) {
                    return 0;
                }

                if (!WORD_FREQUENCY.contains(a)) {
                    return 1;
                }

                if (!WORD_FREQUENCY.contains(b)) {
                    return -1;
                }
                return WORD_FREQUENCY.indexOf(a) < WORD_FREQUENCY.indexOf(b) ? -1 : 1;
            }
        });
    }

    /**
     * This method returns list of possible correct words.
     * @param word Word similar to which we will find.
     * @param tol maximum number of variance allowed in length.
     * @return A map of lists of words, whose key is edit-length.
     */
    private static Map<Integer, List<String>> suggest(String word, int tol) {
        if (null == word || word.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'word' is not valid");
        }

        final int len = word.length();
        int[][] matrix = new int[len][len + tol];
        Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
        for (int i = 0; i <= MAX_EDIT_DISTANCE; i++) {
            result.put(i, new ArrayList<String>());
        }
        for (int i = 0; i <= tol; i++) {
            if (0 != i) {
                doSuggest(word, len + i, result, matrix);
                doSuggest(word, len - i, result, matrix);
            } else {
                doSuggest(word, len, result, matrix);
            }
        }
        return result;
    }

    /**
     * This method search similar words with given length <code>len</code>.
     * @param word word to search.
     * @param len from words of length <code>len</code> to search
     * @param result suggest result.
     * @param matrix shared matrix for underlying dynamic programming algorithm implementation.
     */
    private static void doSuggest(String word, int len, Map<Integer, List<String>> result, int[][] matrix) {
        if (len < 1) {
            return;
        }
        Set<String> candidateWords = WORDS.get(len);
        for (String candidateWord : candidateWords) {
            int editLength = EditDistanceComputer.computeDamerauLevenshteinDistance(word, candidateWord, matrix);
            if (editLength <= MAX_EDIT_DISTANCE) {
                result.get(editLength).add(candidateWord);
            }
            //ignore edit distance > MAX_EDIT_DISTANCE.
        }

    }


    public static boolean isWord(String word) {
        return WORDS.get(word.length()).contains(word);
    }


    public static Dictionary getInstance() {
        return DictionaryHolder.INSTANCE;
    }

}
