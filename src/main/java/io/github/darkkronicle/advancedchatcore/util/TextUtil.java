/*
 * Copyright (C) 2021-2022 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import fi.dy.masa.malilib.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import lombok.experimental.UtilityClass;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

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

    private TreeMap<StringMatch, StringInsert> filterMatches(
            Map<StringMatch, StringInsert> matches) {
        // Filters through matches that don't make sense
        TreeMap<StringMatch, StringInsert> map = new TreeMap<>(matches);
        Iterator<StringMatch> search = new TreeMap<>(map).keySet().iterator();
        int lastEnd = 0;
        while (search.hasNext()) {
            StringMatch m = search.next();
            // Remove overlaps
            if (m.start < lastEnd) {
                map.remove(m);
            } else {
                lastEnd = m.end;
            }
        }
        return map;
    }

    /**
     * Complex method used to split up the split text in this class and replace matches to a string.
     *
     * @param matches Map containing a match and a FluidText provider
     */
    public Text replaceStrings(Text input, Map<StringMatch, StringInsert> matches) {
        // If there's no matches nothing should get replaced.
        if (matches.size() == 0) {
            return input;
        }
        // Sort the matches and then get a nice easy iterator for navigation
        Iterator<Map.Entry<StringMatch, StringInsert>> sortedMatches =
                filterMatches(matches).entrySet().iterator();
        if (!sortedMatches.hasNext()) {
            return input;
        }
        // List of new RawText to form a new FluidText.
        ArrayList<Text> newSiblings = new ArrayList<>();
        // What match this is currently on.
        Map.Entry<StringMatch, StringInsert> match = sortedMatches.next();

        // Total number of chars went through. Used to find where the match end and beginning is.
        int totalchar = 0;
        boolean inMatch = false;
        for (Text text : input.getSiblings()) {
            if (text.getString() == null || text.getString().length() <= 0) {
                continue;
            }
            if (match == null) {
                // No more replacing...
                newSiblings.add(text);
                continue;
            }
            int length = text.getString().length();
            int last = 0;
            while (true) {
                if (length + totalchar <= match.getKey().start) {
                    newSiblings.add(MutableText.of(new LiteralTextContent((text.getString().substring(last)))).fillStyle(text.getStyle()));
                    break;
                }
                int start = match.getKey().start - totalchar;
                int end = match.getKey().end - totalchar;
                if (inMatch) {
                    if (end <= length) {
                        inMatch = false;
                        newSiblings.add(MutableText.of(new LiteralTextContent((text.getString().substring(end)))).fillStyle(text.getStyle()));
                        last = end;
                        if (!sortedMatches.hasNext()) {
                            match = null;
                            break;
                        }
                        match = sortedMatches.next();
                    } else {
                        break;
                    }
                } else if (start < length) {
                    // End will go onto another string
                    if (start > 0) {
                        // Add previous string section
                        newSiblings.add(MutableText.of(new LiteralTextContent((text.getString().substring(last, start)))).fillStyle(text.getStyle()));
                    }
                    if (end >= length) {
                        newSiblings.addAll(
                                match.getValue().getText(text, match.getKey()).getSiblings());
                        if (end == length) {
                            if (!sortedMatches.hasNext()) {
                                match = null;
                                break;
                            }
                            match = sortedMatches.next();
                        } else {
                            inMatch = true;
                        }
                        break;
                    }
                    newSiblings.addAll(
                            match.getValue().getText(text, match.getKey()).getSiblings());
                    if (!sortedMatches.hasNext()) {
                        match = null;
                    } else {
                        match = sortedMatches.next();
                    }
                    last = end;
                    if (match == null || match.getKey().start - totalchar > length) {
                        newSiblings.add(MutableText.of(new LiteralTextContent((text.getString().substring(end)))).fillStyle(text.getStyle()));
                        break;
                    }
                } else {
                    break;
                }
                if (match == null) {
                    break;
                }
            }
            totalchar = totalchar + length;
        }

        MutableText newtext = MutableText.of(TextContent.EMPTY);
        for (Text sibling : newSiblings) {
            newtext.append(sibling);
        }
        return newtext;
    }

    /**
     * Splits off the text that is held by a {@link StringMatch}
     *
     * @param match Match to grab text from
     * @return MutableText of text
     */
    public static MutableText truncate(Text input, StringMatch match) {
        ArrayList<Text> newSiblings = new ArrayList<>();
        boolean start = false;
        // Total number of chars went through. Used to find where the match end and beginning is.
        int totalchar = 0;
        for (Text text : input.getSiblings()) {
            if (text.getContent() == null || text.getString().length() <= 0) {
                continue;
            }

            int length = text.getString().length();

            // Checks to see if current text contains the match.start.
            if (totalchar + length > match.start) {
                if (totalchar + length >= match.end) {
                    if (!start) {
                        newSiblings.add(
                                MutableText.of(new LiteralTextContent(
                                        text.getString()
                                                .substring(
                                                        match.start - totalchar,
                                                        match.end - totalchar))).fillStyle(text.getStyle()));
                    } else {
                        newSiblings.add(
                            MutableText.of(new LiteralTextContent(
                                        text.getString().substring(0, match.end - totalchar))).fillStyle(text.getStyle()));
                    }
                    MutableText newtext = MutableText.of(TextContent.EMPTY);
                    for (Text sibling : newSiblings) {
                        newtext.append(sibling);
                    }
                    return newtext;
                } else {
                    if (!start) {
                        newSiblings.add(
                            MutableText.of(new LiteralTextContent(
                                        text.getString().substring(match.start - totalchar))).fillStyle(text.getStyle()));
                        start = true;
                    } else {
                        newSiblings.add(text);
                    }
                }
            }

            totalchar = totalchar + length;
        }

        // At the end we take the siblings created in this method and override the old ones.
        return null;
    }

    /**
     * See's if style changes for specified fluid text
     * @param text Text to test
     * @return If style changes
     */
    public static boolean styleChanges(Text text) {
        Style style = null;
        if (text.getSiblings().size() == 1) {
            return false;
        }
        for (Text raw : text.getSiblings()) {
            if (style == null) {
                style = raw.getStyle();
            } else if (!style.equals(raw.getStyle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * See's if style changes for specified fluid text
     * @param text Text to test
     * @param predicate Predicate to see if style has changed enough. Previous, current, different
     * @return If style changes
     */
    public static boolean styleChanges(Text text, BiFunction<Style, Style, Boolean> predicate) {
        Style previous = null;
        if (text.getSiblings().size() == 1) {
            return !predicate.apply(text.getSiblings().get(0).getStyle(), text.getSiblings().get(0).getStyle());
        }
        for (Text raw : text.getSiblings()) {
            if (previous == null) {
                previous = raw.getStyle();
            } else if (!previous.equals(raw.getStyle())) {
                if (!predicate.apply(previous, raw.getStyle())) {
                    return true;
                }
                previous = raw.getStyle();
            }
        }
        return false;
    }
}
