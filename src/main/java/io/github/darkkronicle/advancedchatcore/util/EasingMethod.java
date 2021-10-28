package io.github.darkkronicle.advancedchatcore.util;

/** A class to handle an easing method. Examples at https://easings.net/ */
public interface EasingMethod {
    /**
     * Applies the current percentage of the ease.
     *
     * @param x Double from 0-1 (will often clamp at those values)
     * @return The easing value (often clamped at 0-1)
     */
    double apply(double x);

    /** Useful easing methods */
    enum Method implements EasingMethod {
        /** Implements the linear easing function. It returns the same value. x = x */
        LINEAR(x -> x),

        /**
         * Implements the sine easing function.
         *
         * <p>https://easings.net/#easeInSine
         */
        SINE(x -> 1 - Math.cos((x * Math.PI) / 2)),

        /**
         * Implements the quad easing function.
         *
         * <p>https://easings.net/#easeInQuad
         */
        QUAD(x -> x * x),

        /**
         * Implements the quart easing function.
         *
         * <p>https://easings.net/#easeInQuart
         */
        QUART(x -> x * x * x * x),

        /**
         * Implements the circ easing function.
         *
         * <p>https://easings.net/#easeInCirc
         */
        CIRC(x -> 1 - Math.sqrt(1 - Math.pow(x, 2)));

        private final EasingMethod method;

        Method(EasingMethod method) {
            this.method = method;
        }

        @Override
        public double apply(double x) {
            if (x < 0) {
                return 0;
            } else if (x > 1) {
                return 1;
            }
            return method.apply(x);
        }
    }
}
