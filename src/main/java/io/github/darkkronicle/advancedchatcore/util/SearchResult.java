/*
 * Mozilla Public License v2.0
 *
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/** An object that holds information about a search. */
@Environment(EnvType.CLIENT)
public class SearchResult {

    /** All the {@link StringMatch} that were found in the input */
    @Getter private final List<StringMatch> matches;

    /** The {@link Matcher} that was used to find the matches */
    @Getter private final Matcher matcher;

    /** The input string */
    @Getter private final String input;

    /** The condition search */
    @Getter private final String search;

    /**
     * Constructs a search result based off of found information.
     *
     * @param input Input to search
     * @param search Search value
     * @param matcher {@link Matcher} used to serach
     * @param matches {@link List} of {@link StringMatch} that were found
     */
    public SearchResult(String input, String search, Matcher matcher, List<StringMatch> matches) {
        this.input = input;
        this.search = search;
        this.matcher = matcher;
        this.matches = new ArrayList<>(matches);
        Collections.sort(this.matches);
    }

    public int size() {
        return matches.size();
    }

    /**
     * Replaces the groups with a specified match
     *
     * @param string Contents to replace to
     * @param onlyFirst If it will replace/return only the first group. If false it will return the
     *     full string.
     * @return The replaced values in context. If onlyFirst it will only do the context of the first
     *     group.
     */
    public String getGroupReplacements(String string, boolean onlyFirst) {
        if (onlyFirst) {
            int start = matches.get(0).start;
            int end = matches.get(0).end;
            try {
                return matcher.pattern().matcher(input.substring(start, end)).replaceAll(string);
            } catch (Exception e) {
                // Didn't work
                return input.substring(start, end);
            }
        }
        try {
            return matcher.replaceAll(string);
        } catch (Exception e) {
            // Didn't work
            return string;
        }
    }

    /**
     * A method to construct a SearchResult based off of an input and a {@link Matcher} to reuse.
     *
     * @param input Input string to search
     * @param oldMatcher {@link Matcher} containing pattern and search
     * @return A SearchResult with the new matches
     */
    public static SearchResult searchOf(String input, Matcher oldMatcher) {
        // Grab pattern and make new matcher
        Pattern pattern = oldMatcher.pattern();
        Matcher matcher = pattern.matcher(input);
        List<StringMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            // Compile all the matches
            matches.add(new StringMatch(matcher.group(), matcher.start(), matcher.end()));
        }
        // Reset back to the begging
        matcher.reset();
        return new SearchResult(input, pattern.pattern(), matcher, matches);
    }

    /**
     * A method to construct a SearchResult based off of an input, condition, and {@link FindType}
     *
     * @param input Input string to match from
     * @param match Search string
     * @param type {@link FindType} way to search
     * @return SearchResult with compiled searches
     */
    public static SearchResult searchOf(String input, String match, FindType type) {
        Pattern pattern = SearchUtils.compilePattern(match, type);
        Matcher matcher = pattern.matcher(input);
        List<StringMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new StringMatch(matcher.group(), matcher.start(), matcher.end()));
        }
        matcher.reset();
        return new SearchResult(input, match, matcher, matches);
    }
}
