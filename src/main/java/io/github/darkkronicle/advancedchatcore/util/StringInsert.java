package io.github.darkkronicle.advancedchatcore.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * An interface to provide a way to get the text that should be replaced based off of the
 * current {@link Text} and the current {@link StringMatch}
 */
public interface StringInsert {
    /**
     * Return's the {@link MutableText} that should be inserted.
     *
     * @param current The current {@link Text}
     * @param match The current {@link StringMatch}
     * @return
     */
    MutableText getText(Text current, StringMatch match);
}