package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.util.FluidText;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/** An interface for taking text and processing it. */
public interface IMessageProcessor extends IMessageFilter {
    /**
     * Processes text without the unfiltered text.
     *
     * <p>Deprecated because it won't return anything. If unfiltered doesn't exist, insert null into
     * process.
     *
     * @param text Text to modify
     * @return Empty
     */
    @Deprecated
    @Override
    default Optional<FluidText> filter(FluidText text) {
        process(text, null);
        return Optional.empty();
    }

    /**
     * Consumes text.
     *
     * @param text Final text to process
     * @param unfiltered Original text (if available)
     * @return If the processing was a success
     */
    boolean process(FluidText text, @Nullable FluidText unfiltered);
}
