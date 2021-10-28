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

import lombok.AllArgsConstructor;

/**
 * A class to store data about a match.
 *
 * <p>This class is comparable based on where it starts.
 */
@AllArgsConstructor
public class StringMatch implements Comparable<StringMatch> {

    /** The content that was matched */
    public String match;

    /** The index of the start of the match */
    public Integer start;

    /** The index of the end of the match */
    public Integer end;

    @Override
    public int compareTo(StringMatch o) {
        return start.compareTo(o.start);
    }
}
