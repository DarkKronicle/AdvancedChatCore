/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import io.github.darkkronicle.advancedchatcore.finder.RegexFinder;
import io.github.darkkronicle.advancedchatcore.interfaces.IFinder;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** An object that holds information about a search. */
@Environment(EnvType.CLIENT)
public class SearchResult {

    /** All the {@link StringMatch} that were found in the input */
    @Getter private final List<StringMatch> matches;

    /** The finder used to find matches */
    @Getter private final IFinder finder;

    /** The input string */
    @Getter private final String input;

    /** The condition search */
    @Getter private final String search;

    /**
     * Constructs a search result based off of found information.
     *
     * @param input Input to search
     * @param search Search value
     * @param finder {@link IFinder} used to search
     * @param matches {@link List} of {@link StringMatch} that were found
     */
    public SearchResult(String input, String search, IFinder finder, List<StringMatch> matches) {
        this.input = input;
        this.search = search;
        this.finder = finder;
        this.matches = new ArrayList<>(matches);
        Collections.sort(this.matches);
    }

    public int size() {
        return matches.size();
    }

    /**
     * Get's a group from the result. This only works if it's a {@link RegexFinder}, otherwise it
     * returns the entire string.
     *
     * @param num Group number
     * @return StringMatch from group. It references the original string.
     */
    public StringMatch getGroup(StringMatch match, int num) {
        if (!matches.contains(match)) {
            return null;
        }
        if (!(finder instanceof RegexFinder)) {
            return match;
        }
        try {
            RegexFinder regex = (RegexFinder) finder;
            Pattern p = regex.getPattern(input);
            if (p == null) {
                return null;
            }
            Matcher matcher = p.matcher(match.match);
            String group = matcher.group(num);
            int start = matcher.start(num);
            int end = matcher.start(num);
            return new StringMatch(group, start, end);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Replaces the groups with a specified match
     *
     * @param string Contents to replace to
     * @param matchIndex If it will replace/return only the first group. -1 will return the full
     *     string.
     * @return The replaced values in context. If onlyFirst it will only do the context of the first
     *     group.
     */
    public String getGroupReplacements(String string, int matchIndex) {
        if (matchIndex >= 0) {
            if (finder instanceof RegexFinder regex) {
                try {
                    Pattern p = regex.getPattern(search);
                    if (p != null) {
                        return
                                p
                                        .matcher(matches.get(matchIndex).match)
                                        .replaceAll(string);
                    }
                } catch (Exception e) {
                    // Didn't work
                }
            }
            return SearchUtils.replaceGroups(Collections.singletonList(matches.get(0)), string);
        }
        if (finder instanceof RegexFinder regex) {
            try {
                Pattern p = regex.getPattern(search);
                if (p != null) {
                    return p.matcher(input).replaceAll(string);
                }
            } catch (Exception e) {
                // Did not work
            }
        }
        return SearchUtils.replaceGroups(matches, string);
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
        IFinder finder = type.getFinder();
        List<StringMatch> matches = finder.getMatches(input, match);
        return new SearchResult(input, match, finder, matches);
    }

    /**
     * A method to construct a SearchResult based off of an input, condition, and {@link FindType}
     *
     * @param input Input string to match from
     * @param match Search text
     * @param type {@link FindType} way to search
     * @return SearchResult with compiled searches
     */
    public static SearchResult searchOf(Text input, String match, FindType type) {
        IFinder finder = type.getFinder();
        List<StringMatch> matches = finder.getMatches(input, match);
        return new SearchResult(input.getString(), match, finder, matches);
    }
}
