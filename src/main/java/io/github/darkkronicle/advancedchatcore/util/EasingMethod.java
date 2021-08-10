package io.github.darkkronicle.advancedchatcore.util;

public interface EasingMethod {

    double apply(double x);

    enum Method implements EasingMethod {
        LINEAR((x) -> x),
        SINE((x) -> 1 - Math.cos((x * Math.PI) / 2)),
        QUAD((x) -> x * x),
        QUART((x) -> x * x * x * x),
        CIRC((x) -> 1 - Math.sqrt(1 - Math.pow(x, 2)))
        ;

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
