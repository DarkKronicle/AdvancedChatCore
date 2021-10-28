/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import io.github.darkkronicle.advancedchatcore.chat.MessageOwner;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;

/**
 * A class used for helping filters find matches and act on them. Helps with Regular Expressions and
 * means that we don't need this in each class.
 */
@Environment(EnvType.CLIENT)
@UtilityClass
public class SearchUtils {

    /**
     * Method to see if there is a match somewhere with a string with an expression. Is similar to
     * {@link #findMatches(String, String, FindType)} just less expensive since it doesn't need to
     * find every match.
     *
     * @param input String to search.
     * @param toMatch Expression to find.
     * @param type How toMatch should be interpreted.
     * @return If a match is found.
     */
    public boolean isMatch(String input, String toMatch, FindType type) {
        if (type == FindType.ALL) {
            return true;
        }
        Pattern pattern = compilePattern(toMatch, type);
        if (pattern == null) {
            return false;
        }
        return pattern.matcher(input).find();
    }

    /**
     * Compiles a {@link Pattern} for the specified {@link FindType}
     *
     * @param toMatch Match string
     * @param type Find type
     * @return Compiled pattern
     */
    public Pattern compilePattern(String toMatch, FindType type) {
        switch (type) {
            case UPPERLOWER:
                return Pattern.compile(Pattern.quote(toMatch), Pattern.CASE_INSENSITIVE);
            case LITERAL:
                return Pattern.compile(Pattern.quote(toMatch));
            case REGEX:
                return Pattern.compile(toMatch);
            case ALL:
                return Pattern.compile(".+");
        }
        return null;
    }

    /**
     * Method to find all matches within a string. Is similar to {@link #isMatch(String, String,
     * FindType)}}. This method just finds every match and returns it.
     *
     * @param input String to search.
     * @param toMatch Expression to find.
     * @param type How toMatch should be interpreted.
     * @return An Optional containing a list of {@link StringMatch}
     */
    public Optional<List<StringMatch>> findMatches(String input, String toMatch, FindType type) {
        if (type == FindType.ALL) {
            return Optional.of(
                    Collections.singletonList(new StringMatch(input, 0, input.length())));
        }
        Pattern pattern = compilePattern(toMatch, type);
        if (pattern == null) {
            return Optional.empty();
        }
        Set<StringMatch> matches = new TreeSet<>();
        Matcher matcher = pattern.matcher(input);
        addMatches(matches, matcher, 1000);
        if (matches.size() != 0) {
            return Optional.of(new ArrayList<>(matches));
        }
        return Optional.empty();
    }

    /**
     * Get's first match found based off of conditions
     *
     * @param input String to search
     * @param toMatch Search content
     * @param type {@link FindType} way to search
     * @return Optional of a {@link StringMatch} if found
     */
    public Optional<StringMatch> getMatch(String input, String toMatch, FindType type) {
        if (type == FindType.ALL) {
            return Optional.of(new StringMatch(input, 0, input.length()));
        }
        Pattern pattern = compilePattern(toMatch, type);
        if (pattern == null) {
            return Optional.empty();
        }
        // Use treeset to sort the matches
        Set<StringMatch> matches = new TreeSet<>();
        Matcher matcher = pattern.matcher(input);
        // Add and sort matches
        addMatches(matches, matcher, 1);
        if (matches.size() != 0) {
            return Optional.of(matches.toArray(new StringMatch[0])[0]);
        }
        return Optional.empty();
    }

    private void addMatches(Set<StringMatch> matches, Matcher matcher, int limit) {
        int i = 0;
        while (matcher.find() && i < limit) {
            matches.add(new StringMatch(matcher.group(), matcher.start(), matcher.end()));
            i++;
        }
    }

    /**
     * Get the author of a message using regex
     *
     * @param networkHandler Network handler to get player data
     * @param text Text to search
     * @return Owner of the message
     */
    public MessageOwner getAuthor(ClientPlayNetworkHandler networkHandler, String text) {
        if (networkHandler == null) {
            return null;
        }
        Optional<List<StringMatch>> words =
                SearchUtils.findMatches(
                        stripColorCodes(text),
                        ConfigStorage.General.MESSAGE_OWNER_REGEX.config.getStringValue(),
                        FindType.REGEX);
        if (!words.isPresent()) {
            return null;
        }
        // Start by just checking names and such
        PlayerListEntry player = null;
        StringMatch match = null;
        for (StringMatch m : words.get()) {
            if (player != null) {
                break;
            }
            for (PlayerListEntry e : networkHandler.getPlayerList()) {
                // Easy mode
                if ((e.getDisplayName() != null
                                && m.match.equals(stripColorCodes(e.getDisplayName().getString())))
                        || m.match.equals(e.getProfile().getName())) {
                    player = e;
                    match = m;
                    break;
                }
            }
        }
        // Check for ***everything***
        HashMap<PlayerListEntry, List<StringMatch>> entryMatches = new HashMap<>();
        for (PlayerListEntry e : networkHandler.getPlayerList()) {
            String name =
                    stripColorCodes(
                            e.getDisplayName() == null
                                    ? e.getProfile().getName()
                                    : e.getDisplayName().getString());
            Optional<List<StringMatch>> nameWords =
                    SearchUtils.findMatches(
                            name,
                            ConfigStorage.General.MESSAGE_OWNER_REGEX.config.getStringValue(),
                            FindType.REGEX);
            if (!nameWords.isPresent()) {
                continue;
            }
            entryMatches.put(e, nameWords.get());
        }
        for (StringMatch m : words.get()) {
            for (Map.Entry<PlayerListEntry, List<StringMatch>> entry : entryMatches.entrySet()) {
                for (StringMatch nm : entry.getValue()) {
                    if (nm.match.equals(m.match)) {
                        if (player != null && match.start <= m.start) {
                            return new MessageOwner(match.match, player);
                        }
                        return new MessageOwner(nm.match, entry.getKey());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Strip color codes from a String
     *
     * @param string String to strip
     * @return String stripped of colorcodes
     */
    public String stripColorCodes(String string) {
        return string.replaceAll("§.", "");
    }

    private final TreeMap<Integer, String> map = new TreeMap<>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    /**
     * Turns a number into a Roman Numeral.
     *
     * <p>Example: 4 -> IV
     *
     * <p>https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java/12968022
     *
     * @param number Example to convert to
     * @return String or Roman Numeral
     */
    public String toRoman(int number) {
        boolean neg = false;
        if (number == 0) {
            return "O";
        }
        if (number < 0) {
            neg = true;
            number = -1 * number;
        }
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        if (neg) {
            return "-" + map.get(l) + toRoman(number - l);
        } else {
            return map.get(l) + toRoman(number - l);
        }
    }
}
