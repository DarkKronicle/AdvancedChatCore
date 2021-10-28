package io.github.darkkronicle.advancedchatcore.interfaces;

import io.github.darkkronicle.advancedchatcore.util.FluidText;
import io.github.darkkronicle.advancedchatcore.util.SearchResult;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to receive text and matches to process.
 *
 * <p>Similar to {@link IMessageProcessor} but it takes matches and can return a {@link Result}
 */
public interface IMatchProcessor extends IMessageProcessor {
    /** Different outcome's the processor can have */
    enum Result {
        FAIL(false, true, false),
        PROCESSED(true, false, false),
        FORCE_FORWARD(true, true, true),
        FORCE_STOP(true, false, true);

        public final boolean success;
        public final boolean forward;
        public final boolean force;

        Result(boolean success, boolean forward, boolean force) {
            this.success = success;
            this.forward = forward;
            this.force = force;
        }

        public static Result getFromBool(boolean success) {
            if (!success) {
                return FAIL;
            }
            return PROCESSED;
        }
    }

    @Override
    default boolean process(FluidText text, FluidText unfiltered) {
        return processMatches(text, unfiltered, null).success;
    }

    /**
     * Process specific matches and return how the rest of the processors should be handled
     *
     * @param text Final text
     * @param unfiltered Unfiltered version of text. If not available null.
     * @param search {@link SearchResult} matches
     * @return The {@link Result} that the method performed
     */
    Result processMatches(
            FluidText text, @Nullable FluidText unfiltered, @Nullable SearchResult search);

    /**
     * Whether or not this processor should only trigger when matches are present. If false {@link
     * SearchResult} can be null.
     *
     * @return If this processor should only trigger when matches are present
     */
    default boolean matchesOnly() {
        return true;
    }
}
