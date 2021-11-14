/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.finder.CustomFinder;
import io.github.darkkronicle.advancedchatcore.finder.LiteralFinder;
import io.github.darkkronicle.advancedchatcore.finder.RegexFinder;
import io.github.darkkronicle.advancedchatcore.finder.UpperLowerFinder;
import io.github.darkkronicle.advancedchatcore.interfaces.IFinder;
import java.util.function.Supplier;

/** Different methods of searching strings for matches. */
public enum FindType implements IConfigOptionListEntry {
    /** An exact match found in the input */
    LITERAL("literal", LiteralFinder::new),

    /** A match found in the input that is case insensitive */
    UPPERLOWER("upperlower", UpperLowerFinder::new),

    /** A regex match found in the input */
    REGEX("regex", RegexFinder::new),

    /**
     * Use custom ones that mods can create. Defined in {@link
     * io.github.darkkronicle.advancedchatcore.finder.CustomFinder}
     */
    CUSTOM("custom", CustomFinder::getInstance);

    /** Serialized name of the {@link FindType} */
    public final String configString;

    private final Supplier<IFinder> finder;

    private static String translate(String key) {
        return StringUtils.translate("advancedchat.config.findtype." + key);
    }

    FindType(String configString, Supplier<IFinder> finder) {
        this.configString = configString;
        this.finder = finder;
    }

    /** Get's the finder associated with this */
    public IFinder getFinder() {
        return finder.get();
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
