/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.finder;

import java.util.regex.Pattern;

public class RegexFinder extends PatternFinder {

    @Override
    public Pattern getPattern(String toMatch) {
        return Pattern.compile(toMatch);
    }
}
