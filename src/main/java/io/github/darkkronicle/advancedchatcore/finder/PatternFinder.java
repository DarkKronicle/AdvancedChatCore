/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.finder;

import io.github.darkkronicle.advancedchatcore.interfaces.IFinder;
import io.github.darkkronicle.advancedchatcore.util.StringMatch;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternFinder implements IFinder {

    public abstract Pattern getPattern(String toMatch);

    @Override
    public boolean isMatch(String input, String toMatch) {
        Pattern pattern = getPattern(toMatch);
        return pattern.matcher(input).find();
    }

    @Override
    public List<StringMatch> getMatches(String input, String toMatch) {
        Pattern pattern = getPattern(toMatch);
        Matcher matcher = pattern.matcher(input);
        List<StringMatch> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(new StringMatch(matcher.group(), matcher.start(), matcher.end()));
        }
        matcher.reset();
        return matches;
    }
}
