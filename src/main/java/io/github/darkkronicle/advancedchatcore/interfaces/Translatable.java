package io.github.darkkronicle.advancedchatcore.interfaces;

import fi.dy.masa.malilib.util.StringUtils;

/**
 * An interface for a translatable object
 */
public interface Translatable {

    /**
     * Translation key of the object
     * @return Translation key
     */
    String getTranslationKey();

    /**
     * Translates the object
     * @return Translated string
     */
    default String translate() {
        return StringUtils.translate(getTranslationKey());
    }

}
