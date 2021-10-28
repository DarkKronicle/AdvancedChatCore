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

import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

/**
 * A helper class that can take a Text, break it up, and put it back together. This breaks up the
 * Text into different {@link RawText}.
 */
@Environment(EnvType.CLIENT)
public class FluidText implements MutableText {

    private ArrayList<RawText> rawTexts = new ArrayList<>();

    @Setter @Getter private ColorUtil.SimpleColor backgroundColor = null;

    /**
     * Takes a Text and splits it into a list of {@link RawText}.
     *
     * @param text text to split into different {@link RawText}
     */
    public FluidText(Text text) {
        text.visit(
                (style, string) -> {
                    rawTexts.add(new RawText(string, style));
                    return Optional.empty();
                },
                Style.EMPTY);
    }

    /**
     * Constructs a FluidText from {@link OrderedText}
     *
     * @param text {@link OrderedText} to convert
     */
    public FluidText(OrderedText text) {
        text.accept(
                (index, style, codePoint) -> {
                    RawText last;
                    if (rawTexts.size() > 0
                            && (last = rawTexts.get(rawTexts.size() - 1))
                                    .getStyle()
                                    .equals(style)) {
                        // Similar styles get grouped to minimize data
                        last.setMessage(
                                last.getMessage() + new String(Character.toChars(codePoint)));
                    } else {
                        rawTexts.add(new RawText(new String(Character.toChars(codePoint)), style));
                    }
                    return true;
                });
    }

    /** Constructs a blank object */
    public FluidText() {}

    /**
     * Constructs FluidText from a single {@link RawText}'s
     *
     * @param base {@link RawText}
     */
    public FluidText(RawText base) {
        rawTexts.add(base);
    }

    /**
     * Constructs FluidText from a list of {@link RawText}'s
     *
     * @param rawTexts {@link Collection} of {@link RawText}
     */
    public FluidText(Collection<RawText> rawTexts) {
        this.rawTexts.addAll(rawTexts);
    }

    /**
     * Takes the FluidText that is stored inside of this class, and puts it into a plain string.
     * Used mainly for debugging and {@link SearchUtils}
     *
     * @return Plain string of just the raw text of held {@link RawText}
     */
    @Override
    public String getString() {
        if (rawTexts.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // FluidText is made of RawTexts that contain the style and message.
        // Add all the messages together and you get the string value.
        for (RawText text : getRawTexts()) {
            stringBuilder.append(text.getMessage());
        }
        return stringBuilder.toString();
    }

    /**
     * Returns the first style. Not recommended to use.
     *
     * @return Style of the first text
     */
    @Override
    public Style getStyle() {
        if (rawTexts.size() == 0) {
            return null;
        }
        return rawTexts.get(0).getStyle();
    }

    /**
     * Same as getString() in this situation
     *
     * @return String value of the FluidText
     */
    @Override
    public String asString() {
        return getString();
    }

    /**
     * Get all the children
     *
     * @return {@link List} of {@link Text}
     */
    @Override
    public List<Text> getSiblings() {
        return new ArrayList<>(rawTexts);
    }

    /**
     * Deep copy of FluidText. This creates a full new object with zero attachments.
     *
     * @return The copied FluidText
     */
    @Override
    public FluidText copy() {
        FluidText newFluidText = new FluidText();
        for (RawText t : rawTexts) {
            newFluidText.append(t.copy(), false);
        }
        return newFluidText;
    }

    /**
     * Returns a copy of FluidText. The children are the same objects.
     *
     * @return New FluidText
     */
    @Override
    public FluidText shallowCopy() {
        return new FluidText(rawTexts);
    }

    /**
     * Goes through each {@link RawText} and applies the text to the {@link
     * net.minecraft.text.StringVisitable.StyledVisitor}
     *
     * @param styledVisitor Visitor that will take in {@link Style} and {@link String}
     * @param style The default {@link Style} which attributes will replace blank ones.
     * @param <T> Type of variable to be used in the visitor.
     * @return Value the visitor returns, or an empty optional.
     */
    @Override
    public <T> Optional<T> visit(StyledVisitor<T> styledVisitor, Style style) {
        if (rawTexts.size() == 0) {
            return Optional.empty();
        }
        Optional<T> optional;
        for (RawText text : rawTexts) {
            optional = styledVisitor.accept(text.getStyle().withParent(style), text.getMessage());
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }

    /**
     * Goes through each {@link RawText} and applies the message to a {@link
     * net.minecraft.text.StringVisitable.Visitor}
     *
     * @param visitor {@link net.minecraft.text.StringVisitable.Visitor} to accept the {@link
     *     String}
     * @param <T> Type of variable to be used in the visitor
     * @return What the visitor returns, or an empty optional.
     */
    @Override
    public <T> Optional<T> visit(Visitor<T> visitor) {
        Optional<T> optional;
        for (RawText text : rawTexts) {
            optional = visitor.accept(text.getMessage());
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }

    @Deprecated
    @Override
    public <T> Optional<T> visitSelf(StyledVisitor<T> visitor, Style style) {
        return Optional.empty();
    }

    @Deprecated
    @Override
    public <T> Optional<T> visitSelf(Visitor<T> visitor) {
        return Optional.empty();
    }

    /**
     * Constructs an {@link OrderedText} from the {@link RawText}'s
     *
     * @return OrderedText
     */
    @Override
    public OrderedText asOrderedText() {
        List<OrderedText> ordered = new ArrayList<>();
        for (RawText t : rawTexts) {
            ordered.add(t.asOrderedText());
        }
        return OrderedText.concat(ordered);
    }

    /**
     * Splits off the text that is held by a {@link StringMatch}
     *
     * @param match Match to grab text from
     * @return FluidText of text
     */
    public FluidText truncate(StringMatch match) {
        ArrayList<RawText> newSiblings = new ArrayList<>();
        boolean start = false;
        // Total number of chars went through. Used to find where the match end and beginning is.
        int totalchar = 0;
        for (RawText text : getRawTexts()) {
            if (text.getMessage() == null || text.getMessage().length() <= 0) {
                continue;
            }

            int length = text.getMessage().length();

            // Checks to see if current text contains the match.start.
            if (totalchar + length > match.start) {
                if (totalchar + length >= match.end) {
                    if (!start) {
                        newSiblings.add(
                                text.withMessage(
                                        text.getMessage()
                                                .substring(
                                                        match.start - totalchar,
                                                        match.end - totalchar)));
                    } else {
                        newSiblings.add(
                                text.withMessage(
                                        text.getMessage().substring(0, match.end - totalchar)));
                    }
                    return new FluidText(newSiblings);
                } else {
                    if (!start) {
                        newSiblings.add(
                                text.withMessage(
                                        text.getMessage().substring(match.start - totalchar)));
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

    @Override
    public MutableText fillStyle(Style styleOverride) {
        for (RawText t : rawTexts) {
            if (t.getStyle().equals(Style.EMPTY)) {
                t.setStyle(styleOverride);
            }
        }
        return this;
    }

    /**
     * Set's the {@link Style} of all the text
     *
     * @param style {@link Style} to set
     * @return this
     */
    @Override
    public FluidText setStyle(Style style) {
        for (RawText t : rawTexts) {
            t.setStyle(style);
        }
        return this;
    }

    /**
     * Append {@link Text} to the FluidText
     *
     * @param text {@link Text} to add
     * @return this
     */
    @Override
    public FluidText append(Text text) {
        append(new RawText(text.getString(), text.getStyle()), false);
        return this;
    }

    /**
     * An interface to provide a way to get the text that should be replaced based off of the
     * current {@link RawText} and the current {@link StringMatch}
     */
    public interface StringInsert {
        /**
         * Return's the {@link FluidText} that should be inserted.
         *
         * @param current The current {@link RawText}
         * @param match The current {@link StringMatch}
         * @return
         */
        FluidText getText(RawText current, StringMatch match);
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
    public void replaceStrings(Map<StringMatch, StringInsert> matches) {
        // If there's no matches nothing should get replaced.
        if (matches.size() == 0) {
            return;
        }
        // Sort the matches and then get a nice easy iterator for navigation
        Iterator<Map.Entry<StringMatch, StringInsert>> sortedMatches =
                filterMatches(matches).entrySet().iterator();
        if (!sortedMatches.hasNext()) {
            return;
        }
        // List of new RawText to form a new FluidText.
        ArrayList<RawText> newSiblings = new ArrayList<>();
        // What match this is currently on.
        Map.Entry<StringMatch, StringInsert> match = sortedMatches.next();

        // Total number of chars went through. Used to find where the match end and beginning is.
        int totalchar = 0;
        boolean inMatch = false;
        for (RawText text : getRawTexts()) {
            if (text.getMessage() == null || text.getMessage().length() <= 0) {
                continue;
            }
            if (match == null) {
                // No more replacing...
                newSiblings.add(text);
                continue;
            }
            int length = text.getMessage().length();
            int last = 0;
            while (true) {
                if (length + totalchar <= match.getKey().start) {
                    newSiblings.add(text.withMessage(text.getMessage().substring(last)));
                    break;
                }
                int start = match.getKey().start - totalchar;
                int end = match.getKey().end - totalchar;
                if (inMatch) {
                    if (end <= length) {
                        inMatch = false;
                        newSiblings.add(text.withMessage(text.getMessage().substring(end)));
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
                        newSiblings.add(text.withMessage(text.getMessage().substring(last, start)));
                    }
                    if (end >= length) {
                        newSiblings.addAll(
                                match.getValue().getText(text, match.getKey()).getRawTexts());
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
                            match.getValue().getText(text, match.getKey()).getRawTexts());
                    if (!sortedMatches.hasNext()) {
                        match = null;
                    } else {
                        match = sortedMatches.next();
                    }
                    last = end;
                    if (match == null || match.getKey().start - totalchar > length) {
                        newSiblings.add(text.withMessage(text.getMessage().substring(end)));
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

        // At the end we take the siblings created in this method and override the old ones.
        rawTexts = newSiblings;
    }

    /**
     * Get's all of the children.
     *
     * @return {@link List} of {@link RawText}
     */
    public List<RawText> getRawTexts() {
        return rawTexts;
    }

    /**
     * Prefixes the time to text
     *
     * @param format Date formatter
     * @param time Current time
     */
    public void addTime(DateTimeFormatter format, LocalTime time) {
        String replaceFormat =
                ConfigStorage.General.TIME_TEXT_FORMAT.config.getStringValue().replaceAll("&", "§");
        ColorUtil.SimpleColor color = ConfigStorage.General.TIME_COLOR.config.getSimpleColor();
        Style style = Style.EMPTY;
        TextColor textColor = TextColor.fromRgb(color.color());
        style = style.withColor(textColor);
        RawText text = new RawText(replaceFormat.replaceAll("%TIME%", time.format(format)), style);
        rawTexts.add(0, text);
    }

    /**
     * Appends a new {@link RawText} to the {@link FluidText}
     *
     * @param text {@link RawText} to add
     * @param copyIfEmpty If the text's style is empty, have it inherit the style of the last {@link
     *     RawText}
     */
    public void append(RawText text, boolean copyIfEmpty) {
        if (rawTexts.size() > 0) {
            RawText last = rawTexts.get(rawTexts.size() - 1);
            // Prevent having a ton of the same siblings in one...
            if (last.getStyle().equals(text.getStyle())
                    || (copyIfEmpty && text.getStyle().equals(Style.EMPTY))) {
                last.setMessage(last.getMessage() + text.getMessage());
            } else {
                rawTexts.add(text);
            }
        } else {
            rawTexts.add(text);
        }
    }
}
