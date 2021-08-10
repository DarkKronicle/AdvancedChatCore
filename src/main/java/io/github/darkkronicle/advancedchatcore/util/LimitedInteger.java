package io.github.darkkronicle.advancedchatcore.util;

import lombok.Getter;
import lombok.Setter;

public class LimitedInteger {
    @Getter
    private Integer value;
    @Getter
    @Setter
    private Integer limit;

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

    public boolean isPossible(int increment) {
        return !(value + increment >= limit);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
