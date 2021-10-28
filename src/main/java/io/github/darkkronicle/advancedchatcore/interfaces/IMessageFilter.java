package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.util.FluidText;
import java.util.Optional;

/** An interface to modify text. */
public interface IMessageFilter {
    /**
     * Modifies text
     *
     * @param text Text to modify
     * @return Modified text. If empty, the text won't be changed.
     */
    Optional<FluidText> filter(FluidText text);
}
