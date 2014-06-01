package org.apache.maven.plugins.checker.core;

public class EditDistanceComputer {

    /**
     * This method calculates the edit distance that allows multiple edit operations including transpositions.
     *
     * @param str1 The first string.
     * @param str2 The second String.
     * @return Number of edits required.
     */
    public static int computeDamerauLevenshteinDistance(String str1, String str2, int[][] data) {
        final int m = str1.length();
        final int n = str2.length();
        int cost;

        if (0 == m) {
            return n;
        }

        if (0 == n) {
            return m;
        }


        int[][] matrix;
        if (null == data || data.length < m + 1 || data[0].length < n + 1) {
            matrix = new int[m + 1][n + 1];
        } else {
            matrix = data;
        }

        for (int i = 0; i < m + 1; i++) {
            matrix[i][0] = i;
        }

        for (int i = 0; i < n + 1; i++) {
            matrix[0][i] = i;
        }

        char ci = ' ', ci_1, cj = ' ', cj_1;

        for (int i = 0; i < m; i++) {
            ci_1 = ci;
            ci = str1.charAt(i);
            for (int j = 0; j < n; j++) {
                cj_1 = cj;
                cj = str2.charAt(j);
                if (ci == cj) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                matrix[i + 1][j + 1] = Math.min(matrix[i][j + 1] + 1, Math.min(matrix[i + 1][j] + 1, matrix[i][j] + cost));

                if (i > 0 && j > 0 && ci == cj_1 && ci_1 == cj) {
                    matrix[i + 1][j + 1] = Math.min(matrix[i][j], matrix[i - 1][j - 1] + cost);

                }
            }
        }

        return matrix[m][n];
    }

    /**
     * This method calculates the Levenshtein distance.
     *
     * @param str1 The first string.
     * @param str2 The second String.
     * @return Number of edits required.
     */
    public static int computeEditDistance(String str1, String str2) {
        final int[][] matrix;
        final int n = str1.length();
        final int m = str2.length();

        int temp = 0;
        char ch1, ch2;

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        matrix = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            ch1 = str1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }

                matrix[i][j] = Math.min(matrix[i - 1][j] + 1,
                        Math.min(matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + temp));
            }
        }
        return matrix[n][m];
    }
}
