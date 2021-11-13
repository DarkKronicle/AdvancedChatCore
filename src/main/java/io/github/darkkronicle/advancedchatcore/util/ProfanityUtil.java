/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.AdvancedChatCore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

/**
 * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
 *
 * @author PimDeWitte
 */
@Environment(EnvType.CLIENT)
public class ProfanityUtil {
    @Getter private final Map<String, String[]> words = new HashMap<>();

    @Getter private int largestWordLength = 0;

    private static final ProfanityUtil INSTANCE = new ProfanityUtil();

    public static ProfanityUtil getInstance() {
        return INSTANCE;
    }

    private ProfanityUtil() {}

    public void loadConfigs() {
        try {
            List<String> lines;
            File file =
                    FileUtils.getConfigDirectory()
                            .toPath()
                            .resolve("advancedchat")
                            .resolve("profanity.txt")
                            .toFile();
            if (!file.exists()) {
                // Use built in
                lines =
                        new BufferedReader(
                                        new InputStreamReader(
                                                AdvancedChatCore.getResource("profanity.txt"),
                                                StandardCharsets.UTF_8))
                                .lines()
                                .collect(Collectors.toList());
            } else {
                lines =
                        new BufferedReader(new FileReader(file))
                                .lines()
                                .collect(Collectors.toList());
            }
            int counter = 0;
            for (String line : lines) {
                counter++;
                String[] content;
                try {
                    content = line.split(",");
                    if (content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[] {};
                    if (content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if (word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch (Exception e) {
                    AdvancedChatCore.LOGGER.log(
                            Level.ERROR, "Error while initializing profanity words", e);
                }
            }
            AdvancedChatCore.LOGGER.log(
                    Level.INFO, "Loaded " + counter + " words to profanity filter.");
        } catch (URISyntaxException | IOException e) {
            AdvancedChatCore.LOGGER.log(Level.ERROR, "Couldn't access profanity.txt", e);
        }
    }

    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks
     * if the word should be ignored (e.g. bass contains the word *ss).
     */
    public List<StringMatch> getBadWords(String input) {
        if (input == null) {
            return new ArrayList<>();
        }

        input = input.replaceAll("1", "i");
        input = input.replaceAll("!", "i");
        input = input.replaceAll("3", "e");
        input = input.replaceAll("4", "a");
        input = input.replaceAll("@", "a");
        input = input.replaceAll("5", "s");
        input = input.replaceAll("7", "t");
        input = input.replaceAll("0", "o");
        input = input.replaceAll("9", "g");

        ArrayList<StringMatch> badWords = new ArrayList<>();
        input = input.toLowerCase();

        // iterate over each letter in the word
        for (int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence
            // is reached, or the max word length is reached.
            for (int offset = 1;
                    offset < (input.length() + 1 - start) && offset < largestWordLength;
                    offset++) {
                String wordToCheck = input.substring(start, start + offset);
                if (words.containsKey(wordToCheck)) {
                    // for example, if you want to say the word bass, that should be possible.
                    String[] ignoreCheck = words.get(wordToCheck);
                    boolean ignore = false;
                    for (String value : ignoreCheck) {
                        if (input.contains(value)) {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore) {
                        badWords.add(new StringMatch(wordToCheck, start, start + offset));
                    }
                }
            }
        }
        return badWords;
    }
}
