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

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Level;

/**
 * https://gist.github.com/PimDeWitte/c04cc17bc5fa9d7e3aee6670d4105941
 *
 * @author PimDeWitte
 */
@Environment(EnvType.CLIENT)
public class ProfanityUtil {
    @Getter private final Map<Float, List<String>> words = new HashMap<>();

    @Getter private int largestWordLength = 0;

    private static final ProfanityUtil INSTANCE = new ProfanityUtil();

    public static ProfanityUtil getInstance() {
        return INSTANCE;
    }

    private ProfanityUtil() {}

    public void loadConfigs() {
        try {
            List<String> lines;
            File file = FileUtils.getConfigDirectory()
                    .toPath()
                    .resolve("advancedchat")
                    .resolve("swear_words.csv")
                    .toFile();

            Reader fileReader;
            if (!file.exists()) {
                // Use built in
                fileReader = new InputStreamReader(AdvancedChatCore.getResource("swear_words.csv"), StandardCharsets.UTF_8);
            } else {
                fileReader = new FileReader(file);
            }
            CSVParser csv = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase());
            int counter = 0;
            for (CSVRecord record : csv) {
                counter++;
                try {
                    String word = record.get("text");
                    float severity = Float.parseFloat(record.get("severity_rating"));

                    if (word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    if (!words.containsKey(severity)) {
                        words.put(severity, new ArrayList<>());
                    }
                    words.get(severity).add(word);

                } catch (Exception e) {
                    AdvancedChatCore.LOGGER.log(
                            Level.ERROR, "Error while initializing profanity words", e);
                }
            }
            AdvancedChatCore.LOGGER.log(
                    Level.INFO, "Loaded " + counter + " words to profanity filter.");
        } catch (URISyntaxException | IOException  e) {
            AdvancedChatCore.LOGGER.log(Level.ERROR, "Error loading swear_words.csv", e);
        }
    }

    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks
     * if the word should be ignored (e.g. bass contains the word *ss).
     */
    public List<StringMatch> getBadWords(String input, float severity, boolean onlyWordBoundaries) {
        if (input == null) {
            return new ArrayList<>();
        }

        List<StringMatch> badWords = new ArrayList<>();
        input = input.toLowerCase();

        List<Integer> wordBoundaries;
        if (onlyWordBoundaries) {
            wordBoundaries = SearchUtils.findMatches(input, "\\b", FindType.REGEX)
                    .map(matches -> matches.stream().map(m -> m.start).toList())
                    .orElseGet(ArrayList::new);
            if (wordBoundaries.size() == 0) {
                return new ArrayList<>();
            }
        } else {
            wordBoundaries = new ArrayList<>();
        }


        List<String> wordsToFind = getAboveSeverity(severity);

        // iterate over each letter in the word
        int boundaryIndex = 0;
        int index = onlyWordBoundaries ? wordBoundaries.get(0) : 0;
        while (index < input.length()) {
            // from each letter, keep going to find bad words until either the end of the sentence
            // is reached, or the max word length is reached.
            for (int offset = 1; offset < (input.length() + 1 - index) && offset < largestWordLength; offset++) {
                String wordToCheck = input.substring(index, index + offset);
                if (wordsToFind.contains(wordToCheck) && (!onlyWordBoundaries || (wordBoundaries.contains(index + offset)))) {
                    // for example, if you want to say the word bass, that should be possible.
                    badWords.add(new StringMatch(wordToCheck, index, index + offset));
                }
            }
            if (onlyWordBoundaries) {
                boundaryIndex++;
                if (boundaryIndex >= wordBoundaries.size()) {
                    break;
                }
                index = wordBoundaries.get(boundaryIndex);
            } else {
                index++;
            }
        }
        return badWords;
    }

    public List<String> getAboveSeverity(float severity) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Float, List<String>> entry : words.entrySet()) {
            if (entry.getKey() >= severity) {
                list.addAll(entry.getValue());
            }
        }
        return list;
    }
}
