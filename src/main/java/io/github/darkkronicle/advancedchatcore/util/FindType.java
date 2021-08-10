package io.github.darkkronicle.advancedchatcore.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum FindType implements IConfigOptionListEntry {
    LITERAL("literal"),
    UPPERLOWER("upperlower"),
    REGEX("regex"),
    ALL("all")
    ;
    public final String configString;

    private static String translate(String key) {
        return StringUtils.translate("advancedchat.config.findtype." + key);
    }

    FindType(String configString) {
        this.configString = configString   ;
    }

    @Override
    public String getStringValue() {
        return configString;
    }

    @Override
    public String getDisplayName() {
        return translate(configString);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
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

    @Override
    public IConfigOptionListEntry fromString(String value) {
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
