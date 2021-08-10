package io.github.darkkronicle.advancedchatcore.util;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public class SearchResult {

    @Getter
    private final List<StringMatch> matches;
    private final Matcher matcher;
    @Getter
    private final String input;
    @Getter
    private final String search;

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

    public String getGroupReplacements(String string, boolean full) {
        if (full) {
            int start = matches.get(0).start;
            int end = matches.get(0).end;
            try {
                return matcher.pattern().matcher(input.substring(start, end)).replaceAll(string);
            } catch (Exception e) {
                return input.substring(start, end);
            }
        }
        try {
            return matcher.replaceAll(string);
        } catch (Exception e) {
            return string;
        }
    }

    public static SearchResult searchOf(String input, Matcher oldMatcher) {
        Pattern pattern = oldMatcher.pattern();
        Matcher matcher = pattern.matcher(input);
        List<StringMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new StringMatch(matcher.group(), matcher.start(), matcher.end()));
        }
        matcher.reset();
        return new SearchResult(input, pattern.pattern(), matcher, matches);
    }

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

    public Matcher getMatcher() {
        return matcher;
    }
}
