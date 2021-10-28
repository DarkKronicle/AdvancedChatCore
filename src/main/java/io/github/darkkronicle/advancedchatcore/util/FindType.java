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

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

/** Different methods of searching strings for matches. */
public enum FindType implements IConfigOptionListEntry {
    /** An exact match found in the input */
    LITERAL("literal"),

    /** A match found in the input that is case insensitive */
    UPPERLOWER("upperlower"),

    /** A regex match found in the input */
    REGEX("regex"),

    /** Will always return true */
    ALL("all");

    /** Serialized name of the {@link FindType} */
    public final String configString;

    private static String translate(String key) {
        return StringUtils.translate("advancedchat.config.findtype." + key);
    }

    FindType(String configString) {
        this.configString = configString;
    }

    /**
     * Get's the serialized name of the object.
     *
     * @return The config string
     */
    @Override
    public String getStringValue() {
        return configString;
    }

    /**
     * Get's the human readable form of the object.
     *
     * @return String that is for the display name.
     */
    @Override
    public String getDisplayName() {
        return translate(configString);
    }

    /**
     * Get's the next {@link FindType} from the previous one.
     *
     * @param forward Should cycle forward
     * @return Next {@link FindType}
     */
    @Override
    public FindType cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            id++;
        } else {
            id--;
        }
        if (id >= values().length) {
            id = 0;
        } else if (id < 0) {
            id = values().length - 1;
        }
        return values()[id % values().length];
    }

    /**
     * De-serializes a string to {@link FindType}
     *
     * @param value Serialized string
     * @return The found {@link FindType}, null if none
     */
    @Override
    public FindType fromString(String value) {
        return fromFindType(value);
    }

    public static FindType fromFindType(String findtype) {
        for (FindType r : FindType.values()) {
            if (r.configString.equals(findtype)) {
                return r;
            }
        }
        return FindType.LITERAL;
    }
}
