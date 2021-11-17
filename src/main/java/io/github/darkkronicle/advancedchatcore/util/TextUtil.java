/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import fi.dy.masa.malilib.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtil {

    private final char[] SUPERSCRIPTS =
            new char[] {
                '\u2070', '\u00B9', '\u00B2', '\u00B3', '\u2074', '\u2075', '\u2076', '\u2077',
                '\u2078', '\u2079'
            };

    /**
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     * <p>https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
     */
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    /** https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java */
    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    /** Get's a superscript from a number */
    public String toSuperscript(int num) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(SUPERSCRIPTS[num % 10]);
        } while ((num /= 10) > 0);
        return sb.reverse().toString();
    }

    /** Get's the maximum width of a list of translations */
    public int getMaxLengthTranslation(Collection<String> translations) {
        return getMaxLengthTranslation(translations.toArray(new String[0]));
    }

    /** Get's the maximum width of a list of translations */
    public int getMaxLengthTranslation(String... translations) {
        List<String> translated = new ArrayList<>();
        for (String translation : translations) {
            translated.add(StringUtils.translate(translation));
        }
        return getMaxLengthString(translated);
    }

    /** Get's the maximum width of a list of strings */
    public int getMaxLengthString(Collection<String> strings) {
        return getMaxLengthString(strings.toArray(new String[0]));
    }

    /** Get's the maximum width of a list of strings */
    public int getMaxLengthString(String... strings) {
        int max = 0;
        for (String text : strings) {
            int width = StringUtils.getStringWidth(text);
            if (width > max) {
                max = width;
            }
        }
        return max;
    }
}
