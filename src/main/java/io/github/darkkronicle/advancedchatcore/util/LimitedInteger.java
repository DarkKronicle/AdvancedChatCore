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

import lombok.Getter;
import lombok.Setter;

/** An object that will enforce limits on an integer value */
public class LimitedInteger {

    @Getter private Integer value;

    @Getter @Setter private Integer limit;

    public LimitedInteger(int limit) {
        this(limit, 0);
    }

    public LimitedInteger(int limit, int start) {
        this.limit = limit;
        this.value = start;
    }

    /**
     * Increment only if the value is less than the limit.
     *
     * @param increment Integer to increment by
     * @return If the increment was successful
     */
    public boolean incrementIfLimited(int increment) {
        if (value >= limit) {
            return false;
        }
        value += increment;
        return true;
    }

    /**
     * Increment the value and then return if the value is over the limit
     *
     * @param increment Integer to increment by
     * @return If the incremented value is greater than the limit
     */
    public boolean incrementAndCheck(int increment) {
        value += increment;
        return value >= limit;
    }

    /**
     * Increments the value if the value to increment by doesn't go over the limit.
     *
     * @param increment Integer to increment by
     * @return Boolean if it sucecessfully incremented.
     */
    public boolean incrementIfPossible(int increment) {
        if ((value + increment) >= limit) {
            return false;
        }
        value += increment;
        return true;
    }

    /**
     * A method to see if it is possible to increment. This does not change any values.
     *
     * @param increment Value to increment
     * @return If it can increment
     */
    public boolean isPossible(int increment) {
        return !(value + increment >= limit);
    }

    /**
     * Force set's the current value. Ignores limits.
     *
     * @param value Integer to set
     */
    public void setValue(int value) {
        this.value = value;
    }
}
