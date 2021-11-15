/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatcore.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import net.minecraft.util.Formatting;

/**
 * A static utility class that helps when dealing with colors that use bit shifting, like Minecraft.
 * In here there is a color storage class that makes getting int and creating new colors simple.
 */
@UtilityClass
public class ColorUtil {

    // Probably my best hope for color...
    // https://github.com/parzivail/StarWarsMod/blob/master/src/main/java/com/parzivail/util/ui/GLPalette.java
    // I don't like color ints :(
    // intToColor and colorToInt from parzivail https://github.com/parzivail (slightly modified to
    // account for Alpha)

    /**
     * Turns a packed RGB color into a Color
     *
     * @param rgb The color to unpack
     * @return The new Color
     */
    public Color intToColor4f(int rgb) {
        int alpha = rgb >> 24 & 0xFF;
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        return new Color(red, green, blue, alpha);
    }

    /**
     * Turns a Color into a packed RGB int
     *
     * @param c The color to pack
     * @return The packed int
     */
    public int colorToInt4f(Color c) {
        int rgb = c.alpha();
        rgb = (rgb << 8) + c.red();
        rgb = (rgb << 8) + c.green();
        rgb = (rgb << 8) + c.blue();
        return rgb;
    }

    public Color fade(Color color, float percent) {
        float alpha = (float) color.alpha();
        return color.withAlpha((int) Math.floor((alpha * percent)));
    }

    public Color colorFromFormatting(Formatting formatting) {
        return new Color(formatting.getColorValue());
    }

    // Deprecated
    @Deprecated
    public SimpleColor intToColor(int rgb) {
        return SimpleColor.fromColor(intToColor4f(rgb));
    }

    @Deprecated
    public int colorToInt(SimpleColor c) {
        return colorToInt4f(c.toColor());
    }

    @Deprecated
    public SimpleColor fade(SimpleColor color, float percent) {
        return SimpleColor.fromColor(fade(color.toColor(), percent));
    }

    @Deprecated
    public SimpleColor fromFormatting(Formatting formatting) {
        return SimpleColor.fromColor(colorFromFormatting(formatting));
    }

    /** @deprecated Use {@link Colors} */
    @Deprecated public final SimpleColor WHITE = new SimpleColor(255, 255, 255, 255);
    /** @deprecated Use {@link Colors} */
    @Deprecated public final SimpleColor BLACK = new SimpleColor(0, 0, 0, 255);
    /** @deprecated Use {@link Colors} */
    @Deprecated public final SimpleColor GRAY = new SimpleColor(128, 128, 128, 255);

    /**
     * Simple class that uses Lombok's many features to simplify. You can convert at anytime from
     * the color, to an int.
     *
     * <p>New class in {@link Color}
     */
    @Deprecated
    @Value
    @Accessors(fluent = true)
    public static class SimpleColor {

        @Getter int red;

        @Getter int green;

        @Getter int blue;

        @Getter
        @With(AccessLevel.PUBLIC)
        int alpha;

        @Getter int color;

        public SimpleColor(int color) {
            this.color = color;
            SimpleColor completeColor = intToColor(color);
            this.red = completeColor.red();
            this.green = completeColor.green();
            this.blue = completeColor.blue();
            this.alpha = completeColor.alpha();
        }

        public SimpleColor(int red, int green, int blue, int alpha) {
            if (red > 255) {
                red = 255;
            }
            if (green > 255) {
                green = 255;
            }
            if (blue > 255) {
                blue = 255;
            }
            if (alpha > 255) {
                alpha = 255;
            }
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
            this.color = colorToInt(this);
        }

        /** Generated for use of Lombok's @With */
        public SimpleColor(int red, int green, int blue, int alpha, int color) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
            this.color = colorToInt(this);
        }

        public String getString() {
            return String.format("#%08X", color);
        }

        /** Will be removed */
        @Deprecated
        public Color toColor() {
            return new Color(red, green, blue, alpha, color);
        }

        /** Will be removed */
        @Deprecated
        public static SimpleColor fromColor(Color color) {
            return new SimpleColor(
                    color.red(), color.green(), color.blue(), color.alpha(), color.color());
        }
    }
}
