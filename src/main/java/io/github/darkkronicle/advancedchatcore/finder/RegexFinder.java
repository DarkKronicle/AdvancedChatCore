/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.finder;

import io.github.darkkronicle.advancedchatcore.util.*;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexFinder extends PatternFinder {

    @Override
    public Pattern getPattern(String toMatch) {
        return Pattern.compile(toMatch);
    }

    @Override
    public List<StringMatch> getMatches(Text input, String toMatch) {
        // Find named groups
        Optional<List<StringMatch>> optionalGroups = SearchUtils.findMatches(toMatch, "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>", FindType.REGEX);
        if (optionalGroups.isEmpty()) {
            // No named groups, go back to just text
            return getMatches(input.getString(), toMatch);
        }
        FluidText text = new FluidText(input);
        String string = text.getString();

        Pattern pattern = getPattern(toMatch);
        Matcher matcher = pattern.matcher(string);

        List<String> groups = optionalGroups.get().stream().map((match) -> match.match.substring(3, match.end - match.start - 1)).filter((match) -> match.startsWith("adv")).toList();
        List<StringMatch> matches = new ArrayList<>();

        while (matcher.find()) {
            String total = matcher.group();
            int start = matcher.start();
            int stop = matcher.end();
            if (groups.isEmpty()) {
                matches.add(new StringMatch(total, start, stop));
                continue;
            }
            boolean stillMatches = true;
            for (String group : groups) {
                try {
                    if (!isAllowed(text, group, matcher)) {
                        stillMatches = false;
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    // Group does not exist
                }
            }
            if (stillMatches) {
                matches.add(new StringMatch(total, start, stop));
            }
        }

        matcher.reset();
        return matches;
    }

    public static boolean isAllowed(FluidText input, String group, Matcher matcher) {
        group = group.toLowerCase(Locale.ROOT);
        String groupText = matcher.group(group);
        String groupCondition = group.substring(3);
        if (groupText == null || groupCondition.isEmpty() || groupText.isEmpty()) {
            return true;
        }
        int start = matcher.start(group);
        int end = matcher.end(group);
        if (groupCondition.startsWith("0")) {
            groupCondition = groupCondition.substring(1);
            while (groupCondition.length() != 0) {
                FluidText truncated = input.truncate(new StringMatch("", start, end));
                char val = groupCondition.charAt(0);
                groupCondition = groupCondition.substring(1);
                if (val == 'l') {
                    if (!FluidText.styleChanges(truncated, (style1, style2) -> style1.isBold() && style2.isBold())) {
                        return true;
                    }
                    continue;
                }
                if (val == 'o') {
                    if (!FluidText.styleChanges(truncated, (style1, style2) -> style1.isItalic() && style2.isItalic())) {
                        return true;
                    }
                    continue;
                }
                if (val == 'k') {
                    if (!FluidText.styleChanges(truncated, (style1, style2) -> style1.isObfuscated() && style2.isObfuscated())) {
                        return true;
                    }
                    continue;
                }
                if (val == 'n') {
                    if (!FluidText.styleChanges(truncated, (style1, style2) -> style1.isUnderlined() && style2.isUnderlined())) {
                        return true;
                    }
                    continue;
                }
                if (val == 'm') {
                    if (!FluidText.styleChanges(truncated, (style1, style2) -> style1.isStrikethrough() && style2.isStrikethrough())) {
                        return true;
                    }
                    continue;
                }
                if (val == 'z') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getClickEvent() != null && style1.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL
                                    && style2.getClickEvent() != null && style2.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (val == 'x') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getClickEvent() != null && style1.getClickEvent().getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD
                                    && style2.getClickEvent() != null && style2.getClickEvent().getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (val == 'y') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getClickEvent() != null && style1.getClickEvent().getAction() == ClickEvent.Action.OPEN_FILE
                                    && style2.getClickEvent() != null && style2.getClickEvent().getAction() == ClickEvent.Action.OPEN_FILE)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (val == 'w') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getClickEvent() != null && style1.getClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND
                                    && style2.getClickEvent() != null && style2.getClickEvent().getAction() == ClickEvent.Action.RUN_COMMAND)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (val == 'v') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getClickEvent() != null && style1.getClickEvent().getAction() == ClickEvent.Action.SUGGEST_COMMAND
                                    && style2.getClickEvent() != null && style2.getClickEvent().getAction() == ClickEvent.Action.SUGGEST_COMMAND)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (val == 'h') {
                    if (!FluidText.styleChanges(
                            truncated, (style1, style2) -> style1.getHoverEvent() != null
                                    && style2.getHoverEvent() != null)
                    ) {
                        return true;
                    }
                    continue;
                }
                if (FluidText.styleChanges(truncated, (style1, style2) -> Objects.equals(style1.getColor(), style2.getColor()))) {
                    continue;
                }
                Style current = truncated.getStyle();
                TextColor color = current.getColor();
                if (color == null) {
                    if (val == 'f') {
                        return true;
                    }
                    continue;
                }
                Formatting formatting = Formatting.byCode(val);
                if (formatting == null || !formatting.isColor()) {
                    continue;
                }
                if (color.getRgb() == formatting.getColorValue()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
